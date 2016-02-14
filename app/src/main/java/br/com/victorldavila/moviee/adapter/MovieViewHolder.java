package br.com.victorldavila.moviee.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;

import br.com.victorldavila.moviee.R;

/**
 * Created by Victor on 11/02/2016.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder{

    public SimpleDraweeView photo;
    public ProgressBar load;

    public MovieViewHolder(View itemView) {
        super(itemView);

        photo = (SimpleDraweeView) itemView.findViewById(R.id.poster);
        load = (ProgressBar) itemView.findViewById(R.id.load1);
    }
}
