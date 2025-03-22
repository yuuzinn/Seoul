package com.example.seoul.common;

import lombok.Getter;

@Getter
public enum SuccessMessage {
    /**
     * USER
     */
    SUCCESS_SIGNUP(2000, "success_signup"),
    SUCCESS_LOGIN(2001, "success_login"),
    SUCCESS_LOGOUT(2002, "success_logout"),
    SUCCESS_SEND_CODE(2003, "success_send_code"),
    SUCCESS_VERIFY_CODE(2004, "success_verify_code"),
    SUCCESS_RESET_PASSWORD(2005, "success_reset_password"),
    /**
     * TAGS
     */
    SUCCESS_GET_TAGS(2006, "success_get_tags"),
    /**
     * SUBWAY
     */
    SUCCESS_SUBWAY_FETCH(2007, "success_subway_fetch"),
    /**
     * POST
     */
    SUCCESS_CREATE_POST(2010, "success_create_post"),
    SUCCESS_UPDATE_POST(2011, "success_update_post"),
    SUCCESS_DELETE_POST(2012, "success_delete_post"),
    SUCCESS_GET_POST_DETAIL(2013, "success_get_post_detail"),
    SUCCESS_GET_POSTS(2014, "success_get_posts"),
    SUCCESS_SEARCH_POSTS(2015, "success_search_posts"),
    SUCCESS_GET_MY_POSTS(2016, "success_get_my_posts"),
    SUCCESS_GET_LIKED_POSTS(2017, "success_get_liked_posts"),
    /**
     * POI
     */
    SUCCESS_SEARCH_POI(2020, "success_search_poi"),
    /**
     * PEDESTRIAN
     */
    SUCCESS_GET_PEDESTRIAN_ROUTE(2022, "success_get_pedestrian_route"),
    /**
     * LIKES
     */
    SUCCESS_LIKE_POST(2023, "success_like_post"),
    SUCCESS_UNLIKE_POST(2024, "success_unlike_post"),

    ;

    private final int code;
    private final String message;

    SuccessMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
