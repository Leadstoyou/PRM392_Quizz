package com.example.apptracnghiem.database;

import android.provider.BaseColumns;

public class Table {
    private Table(){}
    //du lieu bang categories
        public static class CategoriesTable implements BaseColumns{
            public static final String TABLE_NAME = "categories";
            public static final String COLUMN_NAME = "name";
        }
        //du lieu bang question
        public static class QuestionsTable implements BaseColumns{
            //ten bang
            public static final String TABLE_NAME = "questions";
            //cau hoi
            public static final String COLUMN_QUESTION = "question";
            //4 dap an
            public static final String COLUMN_OPTION1 = "option1";
            public static final String COLUMN_OPTION2 = "option2";
            public static final String COLUMN_OPTION3 = "option3";
            public static final String COLUMN_OPTION4 = "option4";
            //dap an
            public static final String COLUMN_ANSWER = "answer";
            //id chuyen muc
            public static final String COLUMN_CATEGORY_ID = "id_categories";
        }

    // Thêm lớp UserTable ở đây
    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
    }

    public static class ScoresTable implements BaseColumns {
        public static final String TABLE_NAME = "scores";
        public static final String COLUMN_EMAIl = "email";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}