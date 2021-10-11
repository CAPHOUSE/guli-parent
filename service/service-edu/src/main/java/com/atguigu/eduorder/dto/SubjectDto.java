package com.atguigu.eduorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {

    private String id;
    private String title;
    private List<SubjectDto> children = new ArrayList<>();
}
