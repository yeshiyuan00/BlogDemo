// IMyService.aidl
package com.yesy.aidl;

import com.yesy.aidl.Student;

// Declare any non-default types here with import statements

interface IMyService {

    List<Student> getStudent();

    //aidl支持基本类型,String,CharSequence,List,Map,其他类型必须用import导入,即使它们可能在同一个包里
    void addStudent(in Student student);
}
