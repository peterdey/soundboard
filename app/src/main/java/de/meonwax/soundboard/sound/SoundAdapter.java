package de.meonwax.soundboard.sound;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Collections;
import java.util.List;

import de.meonwax.soundboard.R;
import de.meonwax.soundboard.activity.MainActivity;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {

    private final Context context;
    private final List<Sound> sounds;
    private boolean showDeleteButtons = false;
    private boolean editMode = false;

    public SoundAdapter(Context context, List<Sound> sounds) {
        this.context = context;
        this.sounds = sounds;
    }

    public void setShowDeleteButtons(boolean show) {
        this.showDeleteButtons = show;
        notifyDataSetChanged();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    @Override
    public SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sound_row, parent, false);
        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SoundViewHolder holder, int position) {
        final Sound sound = sounds.get(position);

        holder.playButton.setText(sound.getName());
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editMode) {
                    ((MainActivity) context).playSound(sound.getId());
                }
            }
        });

        holder.deleteButton.setVisibility(showDeleteButtons ? View.VISIBLE : View.GONE);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage(Html.fromHtml(context.getString(R.string.confirm_remove, sound.getName())))
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity) context).removeSound(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Sound movedSound = sounds.remove(fromPosition);
        sounds.add(toPosition, movedSound);
        notifyItemMoved(fromPosition, toPosition);
    }

    public class SoundViewHolder extends RecyclerView.ViewHolder {
        public Button playButton;
        public ImageButton deleteButton;

        public SoundViewHolder(View itemView) {
            super(itemView);
            playButton = (Button) itemView.findViewById(R.id.sound_play);
            deleteButton = (ImageButton) itemView.findViewById(R.id.sound_delete);
        }
    }
}
