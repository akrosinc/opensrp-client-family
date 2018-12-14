package org.smartregister.family.sample.application;

import android.util.Log;

import com.evernote.android.job.JobManager;
import com.vijay.jsonwizard.activities.JsonFormActivity;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.activity.FamilyWizardFormActivity;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.family.sample.BuildConfig;
import org.smartregister.family.sample.activity.FamilyProfileActivity;
import org.smartregister.family.sample.job.SampleJobCreator;
import org.smartregister.family.sample.repository.SampleRepository;
import org.smartregister.family.sample.util.SampleConstants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleApplication extends DrishtiApplication {
    private static final String TAG = SampleApplication.class.getCanonicalName();

    private static CommonFtsObject commonFtsObject;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        context.updateCommonFtsObject(createCommonFtsObject());

        //Initialize Modules
        CoreLibrary.init(context);
        ConfigurableViewsLibrary.init(context, getRepository());
        FamilyLibrary.init(context, getRepository(), getMetadata(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);

        SyncStatusBroadcastReceiver.init(this);
        LocationHelper.init(Utils.ALLOWED_LEVELS, Utils.DEFAULT_LOCATION_LEVEL);


        //Auto login by default
        String password = "pwd";
        context.session().start(context.session().lengthInMilliseconds());
        context.configuration().getDrishtiApplication().setPassword(password);
        context.session().setPassword(password);


        //init Job Manager
        JobManager.create(this).addJobCreator(new SampleJobCreator());

        sampleUniqueIds();

    }

    @Override
    public void logoutCurrentUser() {
    }

    public static synchronized SampleApplication getInstance() {
        return (SampleApplication) mInstance;
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new SampleRepository(getInstance().getApplicationContext(), context);
            }
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return repository;
    }


    public static CommonFtsObject createCommonFtsObject() {
        if (commonFtsObject == null) {
            commonFtsObject = new CommonFtsObject(getFtsTables());
            for (String ftsTable : commonFtsObject.getTables()) {
                commonFtsObject.updateSearchFields(ftsTable, getFtsSearchFields());
                commonFtsObject.updateSortFields(ftsTable, getFtsSortFields());
            }
        }
        return commonFtsObject;
    }

    private static String[] getFtsTables() {
        return new String[]{SampleConstants.TABLE_NAME.FAMILY};
    }

    private static String[] getFtsSearchFields() {
        return new String[]{DBConstants.KEY.BASE_ENTITY_ID, DBConstants.KEY.FIRST_NAME, DBConstants.KEY.LAST_NAME, DBConstants.KEY.UNIQUE_ID};
    }

    private static String[] getFtsSortFields() {
        return new String[]{DBConstants.KEY.BASE_ENTITY_ID, DBConstants.KEY.FIRST_NAME, DBConstants.KEY.LAST_NAME, DBConstants.KEY
                .LAST_INTERACTED_WITH};
    }

    private FamilyMetadata getMetadata() {
        FamilyMetadata metadata = new FamilyMetadata(FamilyWizardFormActivity.class, JsonFormActivity.class, FamilyProfileActivity.class);
        metadata.updateFamilyRegister(SampleConstants.JSON_FORM.FAMILY_REGISTER, SampleConstants.TABLE_NAME.FAMILY, SampleConstants.EventType.FAMILY_REGISTRATION, SampleConstants.EventType.UPDATE_FAMILY_REGISTRATION, SampleConstants.CONFIGURATION.FAMILY_REGISTER, SampleConstants.RELATIONSHIP.FAMILY_HEAD, SampleConstants.RELATIONSHIP.PRIMARY_CAREGIVER);
        metadata.updateFamilyMemberRegister(SampleConstants.JSON_FORM.FAMILY_MEMBER_REGISTER, SampleConstants.TABLE_NAME.FAMILY_MEMBER, SampleConstants.EventType.FAMILY_MEMBER_REGISTRATION, SampleConstants.EventType.UPDATE_FAMILY_MEMBER_REGISTRATION, SampleConstants.CONFIGURATION.FAMILY_MEMBER_REGISTER, SampleConstants.RELATIONSHIP.FAMILY);
        return metadata;
    }

    private void sampleUniqueIds() {
        List<String> ids = generateIds(20);
        FamilyLibrary.getInstance().getUniqueIdRepository().bulkInserOpenmrsIds(ids);
    }

    private List<String> generateIds(int size) {
        List<String> ids = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < size; i++) {
            Integer randomInt = r.nextInt(1000) + 1;
            ids.add(randomInt.toString());
        }

        return ids;
    }

}