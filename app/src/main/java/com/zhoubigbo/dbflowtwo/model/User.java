package com.zhoubigbo.dbflowtwo.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zhoubigbo.dbflowtwo.AppDatabase;

@Table(database = AppDatabase.class)
public class User extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long tableId;

    @Column
    private String name;

    @Column
    private int age;

  ///  @Column
  //  private long phone;

    public User() {
        super();
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

  //  public long getPhone() {
  //      return phone;
 //   }

 //   public void setPhone(long phone) {
 //       this.phone = phone;
//    }
}
