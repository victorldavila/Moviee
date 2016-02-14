package br.com.victorldavila.moviee.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import br.com.victorldavila.moviee.OnNextPage;
import br.com.victorldavila.moviee.PreviewMovieActivity;
import br.com.victorldavila.moviee.R;
import br.com.victorldavila.moviee.service.MovieeService;
import br.com.victorldavila.moviee.service.model.Movie;

/**
 * Created by Victor on 11/02/2016.
 */
public class MovieRecyclerView extends RecyclerView.Adapter<MovieViewHolder> {

    public static final String MOVIE_KEY = "MOVIE_KEY";

    private ArrayList<Movie> list;
    private OnNextPage listener;
    private Context context;

    public MovieRecyclerView(OnNextPage listener, Context context) {
        this.listener = listener;
        this.context = context;

        list = new ArrayList<Movie>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /* create a new view */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_poster_movie, parent, false);
        MovieViewHolder vh = new MovieViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        configFresco(holder.photo, MovieeService.ImageUrl + list.get(position).getPoster_path(), holder);

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PreviewMovieActivity.class);
                Bundle args = new Bundle();
                args.putInt(MOVIE_KEY, list.get(position).getId());
                intent.putExtras(args);
                context.startActivity(intent);
            }
        });
    }

    private void configFresco(final SimpleDraweeView photoEvent, String path, final MovieViewHolder holder){
        ControllerListener listener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                holder.load.setVisibility(View.GONE);
            }


        };

        Uri uri =  Uri.parse(path);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setControllerListener(listener)
                .setAutoPlayAnimations(true)
                .build();
        photoEvent.setController(controller);
    }

    public void addItem(ArrayList<Movie> itens){
        int size = itens.size();

        for(int i = 0; i < size; i++){
            list.add(itens.get(i));
        }

        notifyDataSetChanged();
    }

    public void resetPage(){
        list = new ArrayList<Movie>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
