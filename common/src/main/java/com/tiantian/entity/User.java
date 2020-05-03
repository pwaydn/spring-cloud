package com.tiantian.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * description:
 */
@Data                        // getSet toString
@AllArgsConstructor          // 全参构造
@NoArgsConstructor           // 无参构造
@Accessors(chain = true)     // 链式风格访问
@SuppressWarnings("serial")  // 压制警告
public class User implements Serializable{
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private Integer status;
    private Date time;

}
