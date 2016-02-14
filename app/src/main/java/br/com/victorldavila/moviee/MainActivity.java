package br.com.victorldavila.moviee;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.victorldavila.moviee.adapter.MovieRecyclerView;
import br.com.victorldavila.moviee.adapter.MovieViewHolder;
import br.com.victorldavila.moviee.service.MovieeService;
import br.com.victorldavila.moviee.service.model.BaseResponse;
import br.com.victorldavila.moviee.service.model.Movie;
import br.com.victorldavila.moviee.util.MovieeApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements OnNextPage{

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private MovieRecyclerView mAdapter;
    private ProgressDialog progressDialog;
    private CoordinatorLayout layout;

    private static int page = 1;
    private static int orderBy = 1;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private boolean request = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setContent();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mais Populares");
    }

    private void setContent() {
        layout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.galleryRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            page++;
                            if(!request) {
                                request = true;
                                nextMoviePage();
                            }
                        }
                    }
                }
            }
        });
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new MovieRecyclerView(this, this);
        mRecyclerView.setAdapter(mAdapter);

        setLoad();
        movieeGetMovies(page);
    }

    private void setLoad() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Carregando fotos...");
        progressDialog.show();
    }

    private void movieeGetMovies(int page) {

        String queryTemp = "";
        if(orderBy == 1)
            queryTemp = "popularity.dec";
        else
            queryTemp = "vote_count.desc";

        Call<BaseResponse<ArrayList<Movie>>> call = ((MovieeApplication)getApplication())
                .getService()
                .getMovies(String.valueOf(page), queryTemp);
        call.enqueue(new Callback<BaseResponse<ArrayList<Movie>>>() {
            @Override
            public void onResponse(Response<BaseResponse<ArrayList<Movie>>> response, Retrofit retrofit) {

                Log.i("Code", String.valueOf(response.code()));
                if (response.code() == MovieeService.SUCCESS_CODE){
                    updateMovies(response.body().getResults());
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

    private void updateMovies(ArrayList<Movie> result) {
        ((MovieRecyclerView)mRecyclerView.getAdapter()).addItem(result);
        progressDialog.dismiss();
        request = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.popularidade) {
            resetPopularidade();
            return true;
        } else if(id == R.id.avaliados){
            resetMaisVotados();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetMaisVotados() {
        orderBy = 2;
        page = 1;
        ((MovieRecyclerView)mRecyclerView.getAdapter()).resetPage();
        nextMoviePage();
        getSupportActionBar().setTitle("Mais Votados");
    }

    private void resetPopularidade() {
        orderBy = 1;
        page = 1;
        ((MovieRecyclerView)mRecyclerView.getAdapter()).resetPage();
        nextMoviePage();
        getSupportActionBar().setTitle("Mais Populares");
    }

    @Override
    public void nextMoviePage() {
        setLoad();
        movieeGetMovies(page);
    }
}
