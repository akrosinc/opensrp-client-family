package org.smartregister.family;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.family.sync.FamilyClientProcessorForJava;
import org.smartregister.repository.Repository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.helper.ECSyncHelper;

import id.zelory.compressor.Compressor;

/**
 * Created by keyman on 31/07/17.
 */
public class FamilyLibrary {
    private static FamilyLibrary instance;

    private final Context context;
    private final Repository repository;
    private final FamilyMetadata metadata;

    private int applicationVersion;
    private int databaseVersion;

    private UniqueIdRepository uniqueIdRepository;
    private ECSyncHelper syncHelper;

    private FamilyClientProcessorForJava clientProcessorForJava;
    private Compressor compressor;

    public static void init(Context context, Repository repository, FamilyMetadata familyMetadata, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new FamilyLibrary(context, repository, familyMetadata, applicationVersion, databaseVersion);
        }
    }

    public static FamilyLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call "
                    + CoreLibrary.class.getName()
                    + ".init method in the onCreate method of "
                    + "your Application class ");
        }
        return instance;
    }

    private FamilyLibrary(Context contextArg, Repository repositoryArg, FamilyMetadata metadataArg, int applicationVersion, int databaseVersion) {
        this.context = contextArg;
        this.repository = repositoryArg;
        this.metadata = metadataArg;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public Context context() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public FamilyMetadata metadata() {
        return metadata;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public UniqueIdRepository getUniqueIdRepository() {
        if (uniqueIdRepository == null) {
            uniqueIdRepository = new UniqueIdRepository(getRepository());
        }
        return uniqueIdRepository;
    }

    public ECSyncHelper getEcSyncHelper() {
        if (syncHelper == null) {
            syncHelper = ECSyncHelper.getInstance(context().applicationContext());
        }
        return syncHelper;
    }


    public FamilyClientProcessorForJava getClientProcessorForJava() {
        if (clientProcessorForJava == null) {
            clientProcessorForJava = FamilyClientProcessorForJava.getInstance(context().applicationContext());
        }
        return clientProcessorForJava;
    }

    public Compressor getCompressor() {
        if (compressor == null) {
            compressor = Compressor.getDefault(context().applicationContext());
        }
        return compressor;
    }

    /**
     * Use this method when testing.
     * It should replace org.smartregister.Context#setInstance(org.smartregister.Context, org.smartregister.repository.Repository) which has been removed
     *
     * @param context
     */
    public static void reset(Context context, Repository repository, FamilyMetadata metadata, int applicationVersion, int databaseVersion) {
        if (context != null) {
            instance = new FamilyLibrary(context, repository, metadata, applicationVersion, databaseVersion);
        }
    }
}
