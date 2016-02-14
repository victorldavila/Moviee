package br.com.victorldavila.moviee.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import br.com.victorldavila.moviee.R;

/**
 * Created by Victor on 14/02/2016.
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView image;
    public TextView title;
    public ProgressBar load;

    public TrailerViewHolder(View itemView) {
        super(itemView);

        image = (SimpleDraweeView) itemView.findViewById(R.id.trailer);
        title = (TextView) itemView.findViewById(R.id.title);
        load = (ProgressBar) itemView.findViewById(R.id.load1);
    }
}
