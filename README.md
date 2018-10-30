# CustomViewPath
自定义view之仿支付宝支付成功失败动画的实现

### 序言：最近空闲的时候一直在学习自定义View的相关知识，这也是LZ最近半年的学习对象，有的时候就是要给自己定下一个小目标，咱们没有王老板的先赚他一个亿这么豪气，也得先有个目标不是。逛博客的时候看到支付宝支付成功失败的动画效果，刚好最近在学习Path的相关知识，就想着实践一下，也巩固一下自己所学的知识，话不多说直接上图。
![](http://upload-images.jianshu.io/upload_images/490111-e2d0ffd303730804.gif?imageMogr2/auto-orient/strip)
### 这个也是公司项目中需要的，之前由于项目紧，直接让UI切了个图，就这样上了，这不太符合我的一贯作风，但是没办法>_<

>#### 首先我们来分解一下这个动作，首先是一段progressDialog，可以看做是在请求数据等待过程，然后成功之后显示成功的动画，失败之后显示失败的动画，那么这里涉及到三个状态，加载中、加载成功和加载失败，这里我们使用枚举来实现这三种状态。首先呢，我们先来实现这个等待的进度条：

>**1、画一个圆，确切的来说是画一段圆弧，然后旋转画布，在此过程中不断修改圆弧的大小，造成一个这样动态的假象：**

	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getPaddingLeft(), getPaddingTop());   //将当前画布的点移到getPaddingLeft,getPaddingTop,后面的操作都以该点作为参照点
        if (mStatus == StatusEnum.Loading) {    //正在加载
            if (startAngle == minAngle) {
                sweepAngle += 6;
            }
            if (sweepAngle >= 300 || startAngle > minAngle) {
                startAngle += 6;
                if (sweepAngle > 20) {
                    sweepAngle -= 6;
                }
            }
            if (startAngle > minAngle + 300) {
                startAngle %= 360;
                minAngle = startAngle;
                sweepAngle = 20;
            }
            canvas.rotate(curAngle += 4, progressRadius, progressRadius);  //旋转的弧长为4
            canvas.drawArc(new RectF(0, 0, progressRadius * 2, progressRadius * 2), startAngle, sweepAngle, false, mPaint);
            invalidate();
        ｝
    ｝
>这里`startAngle`表示圆弧的起始角度，`sweepAngle`表示圆弧扫过的角度，`minAngle`是一个过渡值，是为了帮助`startAngle`改变值而用到的。这里用到了画弧度的方法，在上一篇博客中我有细讲这个方法，如果你还不知道的话请移步[Android自定义view之圆形进度条](http://www.jianshu.com/p/45c44ad92d50)，这里还用到了`rotate`方法，来看一下它的源码解释：

	/**
     * Preconcat the current matrix with the specified rotation.
     *
     * @param degrees The amount to rotate, in degrees
     * @param px The x-coord for the pivot point (unchanged by the rotation)
     * @param py The y-coord for the pivot point (unchanged by the rotation)
     */
    public final void rotate(float degrees, float px, float py) {
        translate(px, py);
        rotate(degrees);
        translate(-px, -py);
    }
>这个方法主要是将画布进行旋转，我们可以看到，先是将画布平移到某个点，然后再旋转某个角度，最后再平移回去，这样做的目的是为了让需要旋转的`View`进行中心对称旋转，所以后面传的`PX,PY`值需要是`View`宽高的一半，不信的话你可以去做个实验；说了这么多我们直接来看一下效果：

![](http://upload-images.jianshu.io/upload_images/490111-1fe638b87c537a6d.gif?imageMogr2/auto-orient/strip)
>**2、画成功状态的动画，这部分也可以分成两个小部分，先是画一个圆，然后再画中间的钩：**
>(1)、画圆：上一篇博客中讲了通过进度来画弧进而来画整个圆，今天我们的主角是`Path`，所以我们使用`Path`来实现这样一个效果，还是先上代码，通过代码来讲解：

	//追踪Path的坐标
    private PathMeasure mPathMeasure;
    //画圆的Path
    private Path mPathCircle;
    //截取PathMeasure中的path
    private Path mPathCircleDst;
    
	mPaint.setColor(loadSuccessColor);
    mPathCircle.addCircle(getWidth() / 2, getWidth() / 2, progressRadius, Path.Direction.CW);
    mPathMeasure.setPath(mPathCircle, false);
    mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), mPathCircleDst, true);
    canvas.drawPath(mPathCircleDst, mPaint);

>`Path`的常用方法有，`add`一条路径（任意形状，任意线条），这里我们在path中添加了一个圆的路径，具体`Path`常见的用法如下表所示：
	
| Path的常见方法        | 方法含义           |
| ------------- |:-------------:|
|moveTo()      | 该方法移动后续操作的起点坐标 |
|lineTo()      | 该方法是连接起始点与某一点（传的参数）形成一条线|
|setLastPath() | 该方法是设置Path最后的坐标      |
|close()       | 该方法是将起点坐标与终点坐标连接起来形成一个闭合的图形(如果始终点左边能连接的话)    |
|addRect()     | 该方法是绘制一个巨型 |
|addRoundRect()| 该方法是绘制一个圆角矩形      |
|addOval()     | 该方法是绘制一个椭圆      |
|arcTo()       | 该方法是绘制一段圆弧    |
|addArc()      | 该方法是绘制一段圆弧    |

>然后呢，这里有一个`PathMeasure`，简单点说，这玩意就是用来实现`Path`坐标点的追踪，你也可以认为是`Path`坐标的计算器，具体`PathMeasure`的常见的用法如下表所示：

| PathMeasure的常见方法        | 方法含义           |
| -------------------------- |:-----------------:|
|setPath()      | 该方法将path与PathMeasure绑定起来 |
|getLength()    | 该方法用于获得path路径的长度       |
|getSegment()   | 该方法用于截取整个Path的片段       |
|nextContour()  | 该方法用于切换到下一个路径         |

>这里我们通过动画从`0——1`之间的变化，来改变所画圆的弧度：

	circleAnimator = ValueAnimator.ofFloat(0, 1);
    circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            circleValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    });
>(2)、接下来画完圆之后，我们要开始画对钩了：

![对钩](http://upload-images.jianshu.io/upload_images/490111-e43ea22015a188bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
	
	if (circleValue == 1) {      //表示圆画完了,可以钩了
        successPath.moveTo(getWidth() / 1 * 3, getWidth() / 2);
        successPath.lineTo(getWidth() / 2, getWidth() / 5 * 3);
        successPath.lineTo(getWidth() / 3 * 2, getWidth() / 5 * 2);
        mPathMeasure.nextContour();
        mPathMeasure.setPath(successPath, false);
        mPathMeasure.getSegment(0, successValue * mPathMeasure.getLength(), mPathCircleDst, true);
        canvas.drawPath(mPathCircleDst, mPaint);
    }
>这里的坐标我是根据UI给的图大致算出来的，可以参考下面这张图的虚线和实现，对钩的起始坐标在坐标轴中大致是`getWidth() / 8 * 3`，你也可以根据你的需求来画出这个对钩，其实就是两段路径，分别用`path`的`lineTo`方法来实现：

![成功画对勾](http://upload-images.jianshu.io/upload_images/490111-b5814810d1f65f55.gif?imageMogr2/auto-orient/strip)


>同理，画叉叉也是一样的，只要你算出叉在坐标轴中的坐标就ok了，这里我也给出一张参考图：

![叉叉](http://upload-images.jianshu.io/upload_images/490111-c3218225cb905eb0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

	mPaint.setColor(loadFailureColor);
    mPathCircle.addCircle(getWidth() / 2, getWidth() / 2, progressRadius, Path.Direction.CW);
    mPathMeasure.setPath(mPathCircle, false);
    mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), mPathCircleDst, true);
    canvas.drawPath(mPathCircleDst, mPaint);

    if (circleValue == 1) {  //表示圆画完了,可以画叉叉的右边部分
        failurePathRight.moveTo(getWidth() / 3 * 2, getWidth() / 3);
        failurePathRight.lineTo(getWidth() / 3, getWidth() / 3 * 2);
        mPathMeasure.nextContour();
        mPathMeasure.setPath(failurePathRight, false);
        mPathMeasure.getSegment(0, failValueRight * mPathMeasure.getLength(), mPathCircleDst, true);
        canvas.drawPath(mPathCircleDst, mPaint);
    }

    if (failValueRight == 1) {    //表示叉叉的右边部分画完了,可以画叉叉的左边部分
        failurePathLeft.moveTo(getWidth() / 3, getWidth() / 3);
        failurePathLeft.lineTo(getWidth() / 3 * 2, getWidth() / 3 * 2);
        mPathMeasure.nextContour();
        mPathMeasure.setPath(failurePathLeft, false);
        mPathMeasure.getSegment(0, failValueLeft * mPathMeasure.getLength(), mPathCircleDst, true);
        canvas.drawPath(mPathCircleDst, mPaint);
    }

![失败画叉叉](http://upload-images.jianshu.io/upload_images/490111-1a478dbc1d49f3a8.gif?imageMogr2/auto-orient/strip)

### 公众号

欢迎关注我的个人公众号【IT先森养成记】，专注大前端技术分享，包含Android，Java基础，Kotlin，HTML，CSS，JS等技术；在这里你能得到的不止是技术上的提升，还有一些学习经验以及志同道合的朋友，赶快加入我们，一起学习，一起进化吧！！！

![公众号：IT先森养成记](http://upload-images.jianshu.io/upload_images/490111-cfc591d001bf4cc6.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
