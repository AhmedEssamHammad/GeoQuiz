package com.example.digitalwellnez.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String CORRECT_ANSWERS = "correct";
    private static final String WRONG_ANSWERS = "wrong";
    private static final String LAST_QUESTION = "last";
    private static final String IS_CEATER = "isCheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton, mFalseButton, mCheatButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int mCorrectAnswers = 0;
    private int mWrongAnswers = 0;
    private int mLastQuestion = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCorrectAnswers = savedInstanceState.getInt(CORRECT_ANSWERS, 0);
            mWrongAnswers = savedInstanceState.getInt(WRONG_ANSWERS, 0);
            mLastQuestion = savedInstanceState.getInt(LAST_QUESTION, 0);
            mIsCheater = savedInstanceState.getBoolean(IS_CEATER);
        }

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                if (mCurrentIndex < mQuestionBank.length - 1){mCurrentIndex++; }
                else{Toast.makeText(QuizActivity.this, "No more Questions! \n Your Score is : "+mCorrectAnswers+"/"+mQuestionBank.length, Toast.LENGTH_SHORT).show();}
                //mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast toast = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 120);
                toast.show();*/
                checkAnswer(false);
                if (mCurrentIndex < mQuestionBank.length - 1){mCurrentIndex++;}
                else{Toast.makeText(QuizActivity.this,"No more Questions! \n Your Score is : "+mCorrectAnswers+"/"+mQuestionBank.length, Toast.LENGTH_SHORT).show();}
                //mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(CORRECT_ANSWERS, mCorrectAnswers);
        savedInstanceState.putInt(WRONG_ANSWERS, mWrongAnswers);
        savedInstanceState.putInt(LAST_QUESTION, mLastQuestion);
        savedInstanceState.putBoolean(IS_CEATER, mIsCheater);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){

        int messageResId = 0;
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        if (mCurrentIndex == 5){mLastQuestion++;};

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        }
        else{
            if(userPressedTrue == answerIsTrue && mLastQuestion < 2)
            {
                messageResId = R.string.correct_toast;
                mCorrectAnswers++;
            }
            if(!userPressedTrue == answerIsTrue && mLastQuestion < 2)
            {
                messageResId = R.string.incorrect_toast;
                mWrongAnswers++;
            }
        }
        if (messageResId != 0){

            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                    .show();
            }

    }
}
