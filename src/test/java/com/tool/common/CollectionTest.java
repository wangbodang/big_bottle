package com.tool.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangb
 * @date 2025/3/31
 * @description TODO: 类描述
 */
@Slf4j
public class CollectionTest {

        @Data
        @AllArgsConstructor
        class Person{
            private String name;
            private int age;
        }

    @Test
    public void test01(){
        List<Person> people = Arrays.asList(
                new Person("Alice", 25),
                new Person("Bob", 30),
                new Person("Alice", 22), // 重名
                new Person("Alice Wong", 24), // 重名
                new Person("Charlie", 21),
                new Person("Charlie", 25)
        );

        long count = people.stream()
                .collect(Collectors.toMap(
                        Person::getName,   // 去重 key（比如 name）
                        p -> p,            // 保留的值（这里是整个对象）
                        (existing, replacement) -> existing // 保留重复时的哪个（保留第一个）
                ))
                .size();  // 最后 map 的 size 就是去重后的 count
        System.out.println(count);
    }

}
