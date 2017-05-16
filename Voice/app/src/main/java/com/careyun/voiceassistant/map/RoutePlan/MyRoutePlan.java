package com.careyun.voiceassistant.map.RoutePlan;

import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

/**
 * Created by Huangmq on 2017/3/27.
 */

public class MyRoutePlan implements OnGetRoutePlanResultListener {


    private Context mContext;
    private RoutePlanSearch mSearch;
    private int distance;

    public  MyRoutePlan(Context mContext){
        this.mContext =  mContext;
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    public void DrivingSearch(){
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "龙泽");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "西单");
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }





    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        List<TaxiInfo> taxiInfo =  drivingRouteResult.getTaxiInfos();
        distance = taxiInfo.get(0).getDistance();
        Log.e("路线距离",String.valueOf(distance));

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }





}
