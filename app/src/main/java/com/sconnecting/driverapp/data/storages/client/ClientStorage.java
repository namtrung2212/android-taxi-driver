package com.sconnecting.driverapp.data.storages.client;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.listener.CompletionWithObject;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by TrungDao on 7/26/16.
 */
public class ClientStorage<T extends BaseModel & RealmModel> {


    public Class<T> clazz;

    public ClientStorage(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    static Realm realm;

    public static  Realm getRealmInstance(){

        if(realm != null)
            return realm;

        Realm.init(AppDelegate.getContext());
        realm =  Realm.getInstance(
                new RealmConfiguration.Builder()
                        .name("SCONNECTING.Client.Realm")
                        .deleteRealmIfMigrationNeeded()
                        .build()
        );

        return realm;
    }

    public void retrieve(List<T> items){

        Realm realm = getRealmInstance();

        realm.beginTransaction();
        for (T item : items) {
            item.setRetrieveAt(new Date());
            realm.insertOrUpdate( item);
        }
        realm.commitTransaction();
    }


    public void retrieve(T item){

        if(item == null)
            return;

        Realm realm = getRealmInstance();

        realm.beginTransaction();

        item.setRetrieveAt(new Date());
        realm.insertOrUpdate(item);

        realm.commitTransaction();

    }


    public void use(List<T> items){

        Realm realm = getRealmInstance();

        realm.beginTransaction();
        for (T item : items) {
            item.setUsedAt(new Date());
            realm.insertOrUpdate(item);
        }
        realm.commitTransaction();

    }

    public void use(T item){

        if(item == null)
            return;

        Realm realm = getRealmInstance();

        realm.beginTransaction();
        item.setUsedAt(new Date());
        realm.insertOrUpdate(item);
        realm.commitTransaction();

    }

    public void getAll(final GetItemsListener listener){

        Realm realm = getRealmInstance();

        final RealmResults<T> results = realm.where(this.clazz).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {

                use(element);

                if(listener != null)
                    listener.onGetItems(true,element);
            }
        });
    }

    public void get(String filter, final GetItemsListener listener ){

        if(listener != null)
            listener.onGetItems(true,null);

    }
    public void getByStringField (String field, String value, final GetItemsListener listener ) {

        Realm realm = getRealmInstance();

        final RealmResults<T> results = realm.where(this.clazz).equalTo(field,value).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {

                use(element);

                if(listener != null)
                    listener.onGetItems(true,element);
            }
        });

    }

    public void getOneByStringField (String field, String value, final GetOneListener listener ) {

        Realm realm = getRealmInstance();

        final T result = realm.where(this.clazz).equalTo(field,value).findFirst();

        if(result != null)
             use(result);


        if(listener != null)
            listener.onGetOne(true,result);

    }


    public void getByIntField (String field, int value, GetItemsListener listener ) {

        Realm realm = getRealmInstance();

        final RealmResults<T> results = realm.where(this.clazz).equalTo(field,value).findAll();

        use(results);


        if(listener != null)
            listener.onGetItems(true,results);


    }

    public void getOneByIntField (String field, int value, final GetOneListener listener ) {

        Realm realm = getRealmInstance();

        final T result = realm.where(this.clazz).equalTo(field,value).findFirst();

        if(result != null)
            use(result);


        if(listener != null)
            listener.onGetOne(true,result);

    }

    public void getByDoubleField (String field, double value, GetItemsListener listener ) {

        Realm realm = getRealmInstance();

        final RealmResults<T> results = realm.where(this.clazz).equalTo(field,value).findAll();

        use(results);


        if(listener != null)
            listener.onGetItems(true,results);


    }

    public void getOneByDoubleField (String field, Double value, final GetOneListener listener ) {

        Realm realm = getRealmInstance();

        final T result = realm.where(this.clazz).equalTo(field,value).findFirst();

        if(result != null)
            use(result);


        if(listener != null)
            listener.onGetOne(true,result);

    }
    public void getById (String id, final GetOneListener listener ) {

        Realm realm = getRealmInstance();

        T item= realm.where(this.clazz).equalTo("id",id).findFirst();


        if(item != null && (item.getId() == null || item.getId().isEmpty() ))
            item = null;


        if( item != null)
             this.use(item);


        if(listener != null)
            listener.onGetOne(true,item);

    }


    public void getOne (String filter,final GetOneListener listener) {
/*
        let realm = try! Realm()
        var item = (filter != nil) ? (realm.objects(T).filter(filter!).first) : (realm.objects(T).first)

        if( item != nil){
            ClientStorage.use(item!)
        }
        if(item?.id == nil){
            item = nil
        }
        completion?(item : item )
        */

    }

    public void save (T obj) {

        if(obj == null)
            return;

        Realm realm = getRealmInstance();

        realm.beginTransaction();
        realm.insertOrUpdate(obj);
        realm.commitTransaction();

    }


    public void save (List<T> items) {

        Realm realm = getRealmInstance();

        realm.beginTransaction();
        for (T obj : items) {
            realm.insertOrUpdate(obj);
        }
        realm.commitTransaction();

    }

    public void saveAndRefresh (T obj, final GetOneListener listener) {

        if(obj == null) {
             if(listener!= null)
                 listener.onGetOne(false,null);
            return;
        }

        Realm realm = getRealmInstance();

        realm.beginTransaction();
        realm.insertOrUpdate(obj);
        realm.commitTransaction();

        getById(obj.getId(), listener);
    }

    public void delete (final T obj) {

        if(obj == null)
            return;

        if(obj.getId() != null)
            deleteById(obj.getId());
    }

    public void deleteById (final String id) {

        if(id == null)
            return;

        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                T item= realm.where(clazz).equalTo("id",id).findFirst();
                if(item != null)
                    ((RealmObject)item).deleteFromRealm();;
            }
        });

    }


    public void delete(String filter, final CompletionWithObject listener){
/*
        if(filter != nil){

            let realm = try! Realm()
            let items = Array(realm.objects(T).filter(filter!) )

            try! realm.write {
                for item in items {
                    realm.delete(item)
                }
            }

            completion?(deleted: items.count)

        }else{
            completion?(deleted: 0)
        }
        */
    }

    public void deleteAll(){

        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                final RealmResults<T> results = realm.where(clazz).findAll();
                ((RealmResults)results).deleteAllFromRealm();

            }
        });
    }

    public void cleanUp(final Double cleanUpDays, final Completion listener){

        Realm realm = getRealmInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                final RealmResults<T> results =realm.where(clazz)
                        .beginGroup()
                        .isNull("useAt")
                        .or()
                        .lessThan("useAt",new Date(new Date().getTime() - (long)(cleanUpDays * 24 * 60 * 60 * 1000)))
                        .endGroup()
                        .findAll();

                ((RealmResults)results).deleteAllFromRealm();
                if(listener != null)
                    listener.onCompleted();

            }
        });

    }


    public void cleanUpIfNeeded() {
       cleanUp(ClientCachingConfig.getCleanUpDays(this.clazz.getSimpleName()),null);
    }

}
