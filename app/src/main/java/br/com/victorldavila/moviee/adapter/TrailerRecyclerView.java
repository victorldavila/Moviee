package br.com.victorldavila.moviee.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
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
import br.com.victorldavila.moviee.R;
import br.com.victorldavila.moviee.service.MovieeService;
import br.com.victorldavila.moviee.service.model.Movie;
import br.com.victorldavila.moviee.service.model.Trailer;

/**
 * Created by Victor on 14/02/2016.
 */
public class TrailerRecyclerView extends RecyclerView.Adapter<TrailerViewHolder> {

    private ArrayList<Trailer> list;
    private Context context;

    public TrailerRecyclerView(Context context) {
        this.context = context;

        list = new ArrayList<Trailer>();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /* create a new view */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trailer_movie, parent, false);
        TrailerViewHolder vh = new TrailerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        if(list.get(position).getSite().equalsIgnoreCase("youtube")) {
            configFresco(MovieeService.VideoImageUrl + list.get(position).getKey() + "/0.jpg", holder);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(
                            Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + list.get(position).getKey())));
                }
            });
        }

        holder.title.setText(list.get(position).getName());
    }

    private void configFresco(String path, final TrailerViewHolder holder){
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
        holder.image.setController(controller);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<Trailer> getList() {
        return list;
    }

    public void setList(ArrayList<Trailer> list) {
        this.list = list;
    }
}
