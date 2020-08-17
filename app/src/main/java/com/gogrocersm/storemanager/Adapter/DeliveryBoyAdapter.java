package com.gogrocersm.storemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gogrocersm.storemanager.Model.NewDeliveryBoyModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.DeliveryBoyListClick;

import java.text.DecimalFormat;
import java.util.List;

public class DeliveryBoyAdapter extends RecyclerView.Adapter<DeliveryBoyAdapter.MySubView> {

    private Context context;
    private List<NewDeliveryBoyModel> boyModels;
    private DeliveryBoyListClick deliveryBoyListClick;
    private int lastSelectedPosition = -1;

    public DeliveryBoyAdapter(Context context, List<NewDeliveryBoyModel> boyModels,DeliveryBoyListClick deliveryBoyListClick) {
        this.context = context;
        this.boyModels = boyModels;
        this.deliveryBoyListClick = deliveryBoyListClick;
    }

    @NonNull
    @Override
    public MySubView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_boy_view,parent,false);
        return new MySubView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MySubView holder, final int position) {
        NewDeliveryBoyModel newDeliveryBoyModel = boyModels.get(position);

        holder.delivery_boy_name.setText(newDeliveryBoyModel.getBoy_name());
        holder.assign_count.setText("( Current assign orders are "+newDeliveryBoyModel.getCount()+" )");
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        holder.km_away.setText("(Rider has "+decimalFormat.format(Double.parseDouble(newDeliveryBoyModel.getDistance()))+" km away from your location)");

        if (lastSelectedPosition==position){
            holder.delivery_id.setChecked(true);
        }else {
            holder.delivery_id.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.delivery_id.isChecked()){
                    holder.delivery_id.setChecked(false);
                }else {
                    lastSelectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    deliveryBoyListClick.onClick(lastSelectedPosition);
                }




            }
        });

    }


    @Override
    public int getItemCount() {
        return boyModels.size();
    }

    public class MySubView extends RecyclerView.ViewHolder{
        TextView delivery_boy_name,assign_count,km_away;
        CheckBox delivery_id;
        public MySubView(@NonNull View itemView) {
            super(itemView);

            delivery_id = itemView.findViewById(R.id.delivery_id);
            delivery_boy_name = itemView.findViewById(R.id.delivery_boy_name);
            assign_count = itemView.findViewById(R.id.assign_count);
            km_away = itemView.findViewById(R.id.km_away);
        }
    }
}
