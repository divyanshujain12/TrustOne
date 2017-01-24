package com.example.deii.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deii.Models.MyOrderModel;
import com.example.deii.Models.ProductsModel;
import com.example.deii.Utils.AlertMessage;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.Utils.SnackBarCallback;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by divyanshu on 6/12/2016.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.TopicsHolder> implements View.OnClickListener, CallBackInterface, SnackBarCallback {
    private ImageLoader loader;
    int pos = 0;
    private ArrayList<MyOrderModel> model;
    private Context context;


    public MyOrderAdapter(Context context, ArrayList<MyOrderModel> model) {
        this.model = model;
        this.context = context;
        loader = new ImageLoader(context);
    }


    public static class TopicsHolder extends RecyclerView.ViewHolder {
        // CardView cv;

        TextView productNameTV, validUptoTV, statusTV, cancelAutoTV;

        TopicsHolder(View itemView) {
            super(itemView);
            //  cv = (CardView) itemView.findViewById(R.id.cv);

            productNameTV = (TextView) itemView.findViewById(R.id.productNameTV);
            validUptoTV = (TextView) itemView.findViewById(R.id.validUptoTV);
            statusTV = (TextView) itemView.findViewById(R.id.statusTV);
            cancelAutoTV = (TextView) itemView.findViewById(R.id.cancelAutoTV);
        }
    }

    @Override
    public MyOrderAdapter.TopicsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_adapter, parent, false);
        TopicsHolder topicsHolder = new TopicsHolder(v);
        return topicsHolder;
    }

    @Override
    public void onBindViewHolder(MyOrderAdapter.TopicsHolder holder, int position) {
        MyOrderModel myOrderModel = model.get(position);

        holder.productNameTV.setText(myOrderModel.getCategory_name());
        holder.validUptoTV.setText(myOrderModel.getValid_upto());
        holder.statusTV.setText(myOrderModel.getStatus());
        holder.cancelAutoTV.setId(position);
        holder.cancelAutoTV.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View v) {
        pos = v.getId();
        AlertMessage.showAlertDialogWithCallBack(context, "ALERT", "Do you really want to cancel?", this);
    }

    @Override
    public void doAction() {
        CallWebService.getInstance(context).hitJSONObjectVolleyWebService(Constants.WebServices.CANCEL_RENEWAL, createJsonForCancelRenewal(pos), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        model.remove(pos);
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {

        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }


    private HashMap<String, String> createJsonForCancelRenewal(int pos) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.EMAIL, MySharedPereference.getInstance().getString(context, Constants.EMAIL_ID));
        hashMap.put(Constants.ORDER_ID, model.get(pos).getOrder_id());

        return hashMap;
    }
}
