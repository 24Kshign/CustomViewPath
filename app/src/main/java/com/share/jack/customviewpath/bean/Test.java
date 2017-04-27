package com.share.jack.customviewpath.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 17/4/27
 */

public class Test {

    public static void main(String[] args) {
        List<TestBeanA> testBeanAList = new ArrayList<>();
        List<TestBeanB> testBeanBList = new ArrayList<>();
        testBeanAList = initTestA();
        TestBeanB testBeanB = new TestBeanB();
        for (int i = 0; i < testBeanAList.size(); i++) {
            testBeanB.setId(testBeanAList.get(i).getId());
            testBeanB.setTitle(testBeanAList.get(i).getTitle());
            testBeanB.setMoney(testBeanAList.get(i).getMoney());
            testBeanB.setNext(testBeanAList.get(i).isNext());
        }
        System.out.println(testBeanBList.size());
    }

    private static List<TestBeanA> initTestA() {
        TestBeanA testBeanA;
        List<TestBeanA> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testBeanA = new TestBeanA();
            testBeanA.setId(i);
            testBeanA.setTitle("24K纯帅" + i);
            testBeanA.setMoney(i * 1.0f);
            testBeanA.setNext(i % 2 == 0);
            testBeanA.setContent(i + "NBA");
            testBeanA.setFf((i + 1) * 1.0f);
            testBeanA.setTime(i + 100000);
            testBeanA.setSuccess(i % 3 == 1);
            list.add(testBeanA);
        }
        return list;
    }
}