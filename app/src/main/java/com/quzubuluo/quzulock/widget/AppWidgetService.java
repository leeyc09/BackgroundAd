package com.quzubuluo.quzulock.widget;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import com.quzubuluo.quzulock.App;
import com.quzubuluo.quzulock.model.PicResponse;
import com.quzubuluo.quzulock.module.BaseResponse;
import com.quzubuluo.quzulock.module.PicReposity;
import com.quzubuluo.quzulock.net.BaseObserver;
import com.quzubuluo.quzulock.net.RxSchedulers;

/*
 * @author : Teemo
 * description : 周期性更新AppWidget的服务
 */

public class AppWidgetService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        PicReposity.getInstance().getPics("15")
                .compose(RxSchedulers.<BaseResponse<List<PicResponse>>>io_main())
                .subscribe(new BaseObserver<BaseResponse<List<PicResponse>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<List<PicResponse>> value) {
                        if (value.getResult() == 0) {
                            int size = value.getData().size();
                            Random random = new Random();
                            loadBitmap(value.getData().get(random.nextInt(size)).getImgUrl());
                        }
                    }
                });
    }

    private void loadBitmap(final String thumbUrl) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap bitmap = Glide.with(AppWidgetService.this).load("http://192.168.188.35:8088/api_images/" + thumbUrl).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                e.onNext(bitmap);
            }
        }).compose(RxSchedulers.<Bitmap>io_main())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Bitmap value) {
                        Intent intent = new Intent();
                        App.setBitmap(value);
                        intent.setAction(QuzuWidget.ACTION_SHOW);
                        AppWidgetService.this.sendBroadcast(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        finishService();
                    }
                });
    }

    private void finishService() {
        finishService();
    }
}