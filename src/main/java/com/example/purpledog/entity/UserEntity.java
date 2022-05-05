package com.example.purpledog.entity;

import com.example.purpledog.dto.UserDto;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @AllArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity implements Persistable<String>{
    @Id
    private String id;

    @Column(nullable = false)
    private String password;

    @Override
    public boolean isNew() {
        return super.getCreatedDate() == null;
    }

    public UserEntity(UserDto userDto){
        this.id = userDto.getId();
        this.password = userDto.getPassword();
    }

    public void updatePassword(String password){
        this.password = password;
    }
}
