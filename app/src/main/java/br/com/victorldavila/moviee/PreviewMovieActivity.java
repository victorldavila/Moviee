package br.com.victorldavila.moviee;

import android.app.ProgressDialog;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;

import br.com.victorldavila.moviee.adapter.MovieRecyclerView;
import br.com.victorldavila.moviee.adapter.MovieViewHolder;
import br.com.victorldavila.moviee.adapter.TrailerRecyclerView;
import br.com.victorldavila.moviee.service.MovieeService;
import br.com.victorldavila.moviee.service.model.BaseResponse;
import br.com.victorldavila.moviee.service.model.InfoMovie;
import br.com.victorldavila.moviee.service.model.Movie;
import br.com.victorldavila.moviee.service.model.Trailer;
import br.com.victorldavila.moviee.util.MovieeApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PreviewMovieActivity extends AppCompatActivity {

    private TextView year;
    private TextView time;
    private TextView points;
    private TextView about;

    private SimpleDraweeView photoPoster;
    private ProgressBar load;
    private ProgressDialog progressDialog;

    private CoordinatorLayout layout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TrailerRecyclerView mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_movie);

        int id = getIntent().getExtras().getInt(MovieRecyclerView.MOVIE_KEY);

        setToolbar();
        setContent();
        setLoad();
        movieeGetMovie(id);
    }

    private void movieeGetMovie(int id) {

        Call<InfoMovie> call = ((MovieeApplication)getApplication())
                .getService()
                .getMovie(String.valueOf(id));
        call.enqueue(new Callback<InfoMovie>() {
            @Override
            public void onResponse(Response<InfoMovie> response, Retrofit retrofit) {

                Log.i("Code", String.valueOf(response.code()));
                if (response.code() == MovieeService.SUCCESS_CODE){
                    updateMovie(response.body());
                }else {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(layout, "Desculpe ocorreu um erro", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setLoad() {
        progressDialog = new ProgressDialog(PreviewMovieActivity.this);
        progressDialog.setMessage("Carregando informações...");
        progressDialog.show();
    }

    private void updateMovie(InfoMovie results) {
        getSupportActionBar().setTitle(results.getTitle());

        time.setText(String.valueOf(results.getRuntime()) + " min");
        points.setText(results.getVote_average() + "/10");
        about.setText(results.getOverview());

        String[] temp = results.getRelease_date().split("-");
        year.setText(temp[0]);

        progressDialog.dismiss();
        setLoad();
        configFresco(MovieeService.ImageUrl + results.getPoster_path());

        getVideos(results.getId());
    }

    private void getVideos(int id) {
        Call<BaseResponse<ArrayList<Trailer>>> call = ((MovieeApplication)getApplication())
                .getService()
                .getVideos(String.valueOf(id));
        call.enqueue(new Callback<BaseResponse<ArrayList<Trailer>>>() {
            @Override
            public void onResponse(Response<BaseResponse<ArrayList<Trailer>>> response, Retrofit retrofit) {

                Log.i("Code", String.valueOf(response.code()));
                if (response.code() == MovieeService.SUCCESS_CODE){
                    updateVideos(response.body().getResults());
                }else {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(layout, "Desculpe ocorreu um erro", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void updateVideos(ArrayList<Trailer> results) {
        ((TrailerRecyclerView)mRecyclerView.getAdapter()).setList(results);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        progressDialog.dismiss();
    }

    private void setContent() {
        load = (ProgressBar) findViewById(R.id.load1);
        photoPoster = (SimpleDraweeView) findViewById(R.id.poster);

        year = (TextView) findViewById(R.id.year);
        time = (TextView) findViewById(R.id.time);
        points = (TextView) findViewById(R.id.points);
        about = (TextView) findViewById(R.id.about);

        layout = (CoordinatorLayout) findViewById(R.id.layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new TrailerRecyclerView(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void configFresco(String path){
        ControllerListener listener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                load.setVisibility(View.GONE);
            }


        };

        Uri uri =  Uri.parse(path);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setControllerListener(listener)
                .setAutoPlayAnimations(true)
                .build();
        photoPoster.setController(controller);
    }

}
