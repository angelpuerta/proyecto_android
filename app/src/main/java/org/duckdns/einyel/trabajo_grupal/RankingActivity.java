package org.duckdns.einyel.trabajo_grupal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.service.App;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity {

    RatingBar mRatingBar;
    TextView mRatingScale;
    EditText mFeedback;
    Button mSendFeedback;

    Long event;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_puntuacion);

        event = getIntent().getLongExtra(DescripcionActivity.EVENTO, 1);
        username = getIntent().getStringExtra("username");

        mRatingBar = findViewById(R.id.ratingBar);
        mRatingScale = findViewById(R.id.ratingTextView);
        mFeedback = findViewById(R.id.commentBox);
        mSendFeedback = findViewById(R.id.btnSubmit);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                textFromRatingChanged(ratingBar, v);
            }
        });

    }

    void textFromRatingChanged(RatingBar ratingBar, float v) {

        mRatingScale.setText(String.valueOf(v));
        switch ((int) ratingBar.getRating()) {
            case 1:
                mRatingScale.setText(getText(R.string.Mala_puntuacion));
                break;
            case 2:
                mRatingScale.setText(getText(R.string.Media_mala_puntuacion));
                break;
            case 3:
                mRatingScale.setText(getText(R.string.Media_puntuacion));
                break;
            case 4:
                mRatingScale.setText(getText(R.string.Media_buena_puntuacion));
                break;
            case 5:
                mRatingScale.setText(getText(R.string.Buena_puntuacion));
                break;
            default:
                mRatingScale.setText("");

        }
    }

    public void clickSubmit(View view) {
        addComentario(new Comment(event, mFeedback.getText().toString(), mRatingBar.getRating(), username));
        this.finish();

    }


    RankingActivity aux = this;

    public void addComentario(Comment comment) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference comentarios = database.getReference("comments").child(event.toString());

        comentarios.child(comment.getC_id().toString()).setValue(comment);
    }

}
