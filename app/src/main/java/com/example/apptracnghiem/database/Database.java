package com.example.apptracnghiem.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.apptracnghiem.model.Category;
import com.example.apptracnghiem.model.Question;
import com.example.apptracnghiem.model.Test;
import com.example.apptracnghiem.model.User;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Question.db";
    private static final int VERSION = 4; // Tăng phiên bản lên 4

    private SQLiteDatabase db;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final String CATEGORIES_TABLE = "CREATE TABLE " +
                Table.CategoriesTable.TABLE_NAME + "( " +
                Table.CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String QUESTIONS_TABLE = "CREATE TABLE " +
                Table.QuestionsTable.TABLE_NAME + " ( " +
                Table.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                Table.QuestionsTable.COLUMN_ANSWER + " INTEGER, " +
                Table.QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + Table.QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                Table.CategoriesTable.TABLE_NAME + "(" + Table.CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        // Thêm bảng User
        final String USER_TABLE = "CREATE TABLE " +
                Table.UserTable.TABLE_NAME + " ( " +
                Table.UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.UserTable.COLUMN_USERNAME + " TEXT UNIQUE, " +
                Table.UserTable.COLUMN_EMAIL + " TEXT UNIQUE, " +
                Table.UserTable.COLUMN_PASSWORD + " TEXT," +
                Table.UserTable.COLUMN_AVATAR + " TEXT" +
                ")";
        final String SQL_CREATE_SCORES_TABLE = "CREATE TABLE " +
                Table.ScoresTable.TABLE_NAME + " (" +
                Table.ScoresTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.ScoresTable.COLUMN_EMAIl + " TEXT, " +
                Table.ScoresTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                Table.ScoresTable.COLUMN_SCORE + " INTEGER, " +
                Table.ScoresTable.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(CATEGORIES_TABLE);
        db.execSQL(QUESTIONS_TABLE);
        db.execSQL(USER_TABLE);
        db.execSQL(SQL_CREATE_SCORES_TABLE);

        CreatCategories();
        CreateQuestions();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table.CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Table.QuestionsTable.TABLE_NAME);
        onCreate(db);

        if (oldVersion < 2) {
            // Thêm câu hỏi mới cho phiên bản 2


            // Thêm các câu hỏi khác tương tự
        }
    }

    private void insertCategories(Category category) {
        ContentValues values = new ContentValues();
        values.put(Table.CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(Table.CategoriesTable.TABLE_NAME, null, values);
    }

    private void CreatCategories() {
        Category c1 = new Category("Ngữ Văn");
        insertCategories(c1);
        Category c2 = new Category("Lịch Sử");
        insertCategories(c2);
        Category c3 = new Category("Địa Lý");
        insertCategories(c3);
    }

    private void insertQuestion(Question question) {
        ContentValues values = new ContentValues();
        values.put(Table.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        values.put(Table.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        values.put(Table.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        values.put(Table.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        values.put(Table.QuestionsTable.COLUMN_OPTION4, question.getOption4());
        values.put(Table.QuestionsTable.COLUMN_ANSWER, question.getAnswer());
        values.put(Table.QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(Table.QuestionsTable.TABLE_NAME, null, values);
    }

    private void CreateQuestions() {
        // Thêm các câu hỏi cũ vào đây
        // Ngữ Văn
        Question q1 = new Question("Ai là tác giả của tập thơ 'Góc sân và khoảng trời'?",
                "A. Trần Đăng Khoa", "B. Xuân Quỳnh", "C. Nguyễn Khoa Điềm", "D. Phạm Tiến Duật", 1, 1);
        insertQuestion(q1);

        Question q2 = new Question("Ai là tác giả của tác phẩm 'Truyện Kiều'?",
                "A. Nguyễn Du", "B. Hồ Xuân Hương", "C. Nguyễn Trãi", "D. Bà Huyện Thanh Quan", 1, 1);
        insertQuestion(q2);

        Question q3 = new Question("Trong bài thơ 'Ông đồ' của Vũ Đình Liên, hình ảnh ông đồ tượng trưng cho điều gì?",
                "A. Sự phát triển của xã hội", "B. Nét đẹp văn hóa truyền thống đang mai một", "C. Sự giao thoa văn hóa Đông - Tây", "D. Cuộc sống hiện đại", 2, 1);
        insertQuestion(q3);

        Question q4 = new Question("Trong câu “Thưa ông, chúng cháu ở Gia Lâm lên đấy ạ. Đi bốn năm hôm mới lên đến đây, vất vả quá!”. Câu nói “Thưa ông” thuộc thành phần gì của câu?",
                "A. Phụ chú", "B. Cảm thán", "C. Gọi đáp", "D. Tình thái", 3, 1);
        insertQuestion(q4);

        Question q5 = new Question("Câu thơ 'Con ơi mẹ dặn câu này/ Chớ nghe lời phỉnh tiếng may của người' thuộc tác phẩm nào?",
                "A. Lục Vân Tiên", "B. Truyện Kiều", "C. Cây tre Việt Nam", "D. Bánh trôi nước", 4, 1);
        insertQuestion(q5);

        Question q6 = new Question("Tác phẩm 'Số đỏ' của Vũ Trọng Phụng thuộc thể loại nào?",
                "A. Truyện ngắn", "B. Tiểu thuyết", "C. Kịch", "D. Thơ", 2, 1);
        insertQuestion(q6);

        Question q7 = new Question("Trong bài thơ 'Tức cảnh Pác Bó', câu thơ nào thể hiện tinh thần lạc quan của Bác Hồ?",
                "A. Sáng ra bờ suối tối vào hang", "B. Cháo bẹ rau măng vẫn sẵn sàng", "C. Cuộc đời cách mạng thật là sang", "D. Bàn đá chông chênh dịch sử Đảng", 3, 1);
        insertQuestion(q7);
        Question q8 = new Question("Tác phẩm 'Tắt đèn' của Ngô Tất Tố phản ánh hiện thực xã hội nào?",
                "A. Xã hội phong kiến Việt Nam đầu thế kỷ 20", "B. Xã hội Việt Nam thời kỳ kháng chiến chống Pháp", "C. Xã hội Việt Nam thời kỳ đổi mới", "D. Xã hội Việt Nam thời kỳ bao cấp", 1, 1);
        insertQuestion(q8);

        Question q9 = new Question("Trong truyện ngắn 'Lão Hạc' của Nam Cao, con vật nào gắn bó với lão Hạc?",
                "A. Con mèo", "B. Con chó", "C. Con gà", "D. Con lợn", 2, 1);
        insertQuestion(q9);
        Question q10 = new Question("Ai là người sáng lập ra nhà nước Văn Lang?",
                "A. An Dương Vương", "B. Kinh Dương Vương", "C. Hùng Vương", "D. Thục Phán", 3, 2);
        insertQuestion(q10);


        // Lịch Sử
        Question q16 = new Question("Cuộc khai thác thuộc địa lần thứ hai (1919-1929) của thực dân Pháp ở Đông Dương được diễn ra trong hoàn cảnh nào?",
                "A. Nước Pháp đang chuyển sang giai đoạn chủ nghĩa đế quốc",
                "B. Nước Pháp bị thiệt hại nặng nề do cuộc chiến tranh xâm lược Việt Nam",
                "C. Nước Pháp bị thiệt hại nặng nề do cuộc chiến tranh thế giới thứ nhất (1914-1918)",
                "D. Tình hình kinh tế, chính trị ở Pháp ổn định", 3, 2);
        insertQuestion(q16);

        Question q17 = new Question("Thực dân Pháp tiến hành cuộc khai thác thuộc địa lần thứ hai ở Đông Dương (1919 - 1929) khi",
                "A. Hệ thống thuộc địa của chủ nghĩa đế quốc tan rã.",
                "B. Thế giới tư bản đang lâm vào khủng hoảng thừa.",
                "C. Cuộc chiến tranh thế giới thứ nhất kết thúc.",
                "D. Kinh tế các nước tư bản đang trên đà phát triển.", 3, 2);
        insertQuestion(q17);

        Question q18 = new Question("Chiến thắng Điện Biên Phủ năm 1954 đã kết thúc cuộc chiến tranh nào của Pháp ở Đông Dương?",
                "A. Chiến tranh thế giới thứ nhất", "B. Chiến tranh thế giới thứ hai", "C. Chiến tranh Đông Dương", "D. Chiến tranh Việt Nam", 3, 2);
        insertQuestion(q18);

        Question q19 = new Question("Ai là người lãnh đạo cuộc khởi nghĩa Lam Sơn chống quân Minh vào thế kỷ 15?",
                "A. Trần Hưng Đạo", "B. Lê Lợi", "C. Quang Trung", "D. Lý Thường Kiệt", 2, 2);
        insertQuestion(q19);

        Question q20 = new Question("Năm bao nhiêu diễn ra sự kiện Cách mạng Tháng Tám thành công?",
                "A. 1940", "B. 1945", "C. 1950", "D. 1954", 2, 2);
        insertQuestion(q20);

        Question q21 = new Question("Triều đại nào đã xây dựng thành nhà Hồ (Tây Đô) ở Thanh Hóa?",
                "A. Nhà Trần", "B. Nhà Hồ", "C. Nhà Lê", "D. Nhà Nguyễn", 2, 2);
        insertQuestion(q21);

        Question q22 = new Question("Hiệp định Genève năm 1954 chia Việt Nam thành mấy vùng tập kết quân sự?",
                "A. 2", "B. 3", "C. 4", "D. 5", 1, 2);
        insertQuestion(q22);

        Question q23 = new Question("Ai là vị vua cuối cùng của triều Nguyễn?",
                "A. Bảo Đại", "B. Minh Mạng", "C. Tự Đức", "D. Gia Long", 1, 2);
        insertQuestion(q23);

        Question q24 = new Question("Trận Bạch Đằng năm 938, đánh bại quân Nam Hán, do ai lãnh đạo?",
                "A. Ngô Quyền", "B. Đinh Bộ Lĩnh", "C. Lê Hoàn", "D. Lý Thường Kiệt", 1, 2);
        insertQuestion(q24);

        Question q25 = new Question("Phong trào Tây Sơn do ai lãnh đạo?",
                "A. Nguyễn Huệ", "B. Nguyễn Nhạc", "C. Nguyễn Lữ", "D. Cả ba anh em Nguyễn Nhạc, Nguyễn Huệ, Nguyễn Lữ", 4, 2);
        insertQuestion(q25);


        // Địa Lý
        Question q31 = new Question("Hang Sơn Đoòng, hang động lớn nhất thế giới, nằm ở tỉnh nào?",
                "A. Quảng Bình", "B. Quảng Trị", "C. Thừa Thiên Huế", "D. Hà Tĩnh", 1, 3);
        insertQuestion(q31);

        Question q32 = new Question("Sông Mekong chảy qua bao nhiêu quốc gia Đông Nam Á?",
                "A. 4", "B. 5", "C. 6", "D. 7", 3, 3);
        insertQuestion(q32);

        Question q33 = new Question("Hà Nội là thủ đô nước nào?",
                "A. Mỹ", "B. Cà Màu", "C.Nam Cực", "D.Việt Nam", 4, 3);
        insertQuestion(q33);

        Question q34 = new Question("Thành phố Đà Nẵng nằm ở vùng nào của Việt Nam?",
                "A. Tây Bắc Bộ", "B. Đông Bắc Bộ", "C. Duyên hải Nam Trung Bộ", "D. Tây Nguyên", 3, 3);
        insertQuestion(q34);

        Question q35 = new Question("Đồng bằng sông Cửu Long được biết đến với loại hình nông nghiệp nào?",
                "A. Trồng lúa nước", "B. Trồng chè", "C. Trồng cà phê", "D. Trồng cao su", 1, 3);
        insertQuestion(q35);

        Question q36 = new Question("Hồ lớn nhất Việt Nam là hồ nào?",
                "A. Hồ Tây", "B. Hồ Ba Bể", "C. Hồ Trị An", "D. Hồ Thác Bà", 2, 3);
        insertQuestion(q36);

        Question q37 = new Question("Vịnh Hạ Long, một di sản thiên nhiên thế giới, nằm ở tỉnh nào?",
                "A. Quảng Ninh", "B. Hải Phòng", "C. Thanh Hóa", "D. Nghệ An", 1, 3);
        insertQuestion(q37);

        Question q38 = new Question("Đỉnh núi Pan-xi-păng có độ cao bao nhiêu mét?",
                "a. 3134 mét.", "b. 3143 mét.", "c. 3314 mét.", "a. 1 mét 2", 2, 3);
        insertQuestion(q38);
        Question q39 = new Question("Dãy núi nào dài nhất Việt Nam?", "A. Trường Sơn", "B. Hoàng Liên Sơn", "C. Con Voi", "D. Tam Đảo", 1, 3);
        insertQuestion(q39);

        Question q40 = new Question("Biển Đông thuộc đại dương nào?", "A. Thái Bình Dương", "B. Ấn Độ Dương", "C. Đại Tây Dương", "D. Bắc Băng Dương", 1, 3);
        insertQuestion(q40);
    }

    @SuppressLint("Range")
    public List<Category> getDataCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Table.CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(Table.CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(Table.CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            }
            while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }
    @SuppressLint("Range")
    public Category getCategoryById(int id) {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Table.CategoriesTable.TABLE_NAME +
                " WHERE " + Table.CategoriesTable._ID + " = ?", new String[]{String.valueOf(id)});

        if (c != null) {
            c.moveToFirst();
            Category category = new Category();
            category.setId(c.getInt(c.getColumnIndex(Table.CategoriesTable._ID)));
            category.setName(c.getString(c.getColumnIndex(Table.CategoriesTable.COLUMN_NAME)));
            c.close();
            return category;
        }
        return null;
    }
    @SuppressLint("Range")
    public ArrayList<Question> getQuestions(int categoryID) {
        ArrayList<Question> questionArrayList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = Table.QuestionsTable.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(categoryID)};

        Cursor c = db.query(Table.QuestionsTable.TABLE_NAME,
                null, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Table.QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(Table.QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(Table.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(Table.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(Table.QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(Table.QuestionsTable.COLUMN_OPTION4)));
                question.setAnswer(c.getInt(c.getColumnIndex(Table.QuestionsTable.COLUMN_ANSWER)));
                question.setCategoryID(c.getInt(c.getColumnIndex(Table.QuestionsTable.COLUMN_CATEGORY_ID)));
                questionArrayList.add(question);
            }
            while (c.moveToNext());
        }
        c.close();
        return questionArrayList;
    }

    public boolean addUser(String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table.UserTable.COLUMN_EMAIL, email);
        values.put(Table.UserTable.COLUMN_USERNAME, username);
        values.put(Table.UserTable.COLUMN_PASSWORD, password);
        long result = db.insert(Table.UserTable.TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Table.UserTable._ID};
        String selection = Table.UserTable.COLUMN_EMAIL + "=? AND " + Table.UserTable.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(Table.UserTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                Table.UserTable._ID,
                Table.UserTable.COLUMN_EMAIL,
                Table.UserTable.COLUMN_USERNAME,
                Table.UserTable.COLUMN_PASSWORD,
                Table.UserTable.COLUMN_AVATAR
        };
        String selection = Table.UserTable.COLUMN_EMAIL + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(Table.UserTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Table.UserTable._ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(Table.UserTable.COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(Table.UserTable.COLUMN_PASSWORD));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow(Table.UserTable.COLUMN_AVATAR));

            user = new User(id, username, email, password,avatar);
            cursor.close();
        }
        return user;
    }
    public boolean updateUser(User user) {
        Log.d("user",String.valueOf(user));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table.UserTable.COLUMN_USERNAME, user.getUsername());
        values.put(Table.UserTable.COLUMN_EMAIL, user.getEmail());
        values.put(Table.UserTable.COLUMN_PASSWORD, user.getPassword());
        values.put(Table.UserTable.COLUMN_AVATAR, user.getAvatar());
        String selection = Table.UserTable._ID + "=?";
        String[] selectionArgs = {String.valueOf(user.get_id())};

        int count = db.update(Table.UserTable.TABLE_NAME, values, selection, selectionArgs);
        return count > 0;
    }
    public void addNewScore(String email, int categoryId, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table.ScoresTable.COLUMN_EMAIl, email);
        values.put(Table.ScoresTable.COLUMN_CATEGORY_ID, categoryId);
        values.put(Table.ScoresTable.COLUMN_SCORE, score);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Log.d("currentTimestamp",String.valueOf(currentTimestamp));
        values.put(Table.ScoresTable.COLUMN_TIMESTAMP, currentTimestamp.toString());

        long newRowId = db.insert(Table.ScoresTable.TABLE_NAME, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public List<Test> getScoreByUserEmail(String email) {
        List<Test> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Đảm bảo tên cột chính xác
        String[] columns = {
                Table.ScoresTable.COLUMN_EMAIl,
                Table.ScoresTable.COLUMN_CATEGORY_ID,
                Table.ScoresTable.COLUMN_SCORE,
                Table.ScoresTable.COLUMN_TIMESTAMP
        };

        Cursor cursor = db.query(
                Table.ScoresTable.TABLE_NAME,
                columns,
                Table.ScoresTable.COLUMN_EMAIl + "=?",
                new String[]{email},
                null,
                null,
                Table.ScoresTable.COLUMN_TIMESTAMP + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
             String username = cursor.getString(cursor.getColumnIndex(Table.ScoresTable.COLUMN_EMAIl));
                int categoryId = cursor.getInt(cursor.getColumnIndex(Table.ScoresTable.COLUMN_CATEGORY_ID));
                int score = cursor.getInt(cursor.getColumnIndex(Table.ScoresTable.COLUMN_SCORE));
                Timestamp timestamp = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(Table.ScoresTable.COLUMN_TIMESTAMP)));

                Test test = new Test(username, categoryId, score, timestamp);
                scoreList.add(test);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return scoreList;
    }
    public Cursor getAllQuestionData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT q.*, c." + Table.CategoriesTable.COLUMN_NAME + " " +
                "FROM " + Table.QuestionsTable.TABLE_NAME + " q " +
                "LEFT JOIN " + Table.CategoriesTable.TABLE_NAME + " c " +
                "ON q." + Table.QuestionsTable.COLUMN_CATEGORY_ID + " = c." + Table.CategoriesTable._ID;
        return db.rawQuery(query, null);
    }

}