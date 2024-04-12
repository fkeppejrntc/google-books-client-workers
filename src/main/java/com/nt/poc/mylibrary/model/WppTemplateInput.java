package com.nt.poc.mylibrary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WppTemplateInput {
    private String type;
    private String text;
//    private Image image;
//
//    public WppTemplateInput(String type, String text) {
//        this.type = type;
//        this.text = text;
//    }
//
//    public WppTemplateInput(String type, Image image) {
//        this.type = type;
//        this.image = image;
//    }
//
//    @Builder
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Image{
//        private String link;
//    }
}
