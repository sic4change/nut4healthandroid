package org.sic4change.nut4health.data;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;

import org.sic4change.nut4health.data.names.DataContractNames;

public class SortUtils {

    /**
     * A raw query at runtime to oder by column for getting all the contracts sorted.
     * @param sortBy
     * @param status
     * @return
     */
    public static SimpleSQLiteQuery getAllQuery(String sortBy, String status) {
        SupportSQLiteQueryBuilder queryBuilder = SupportSQLiteQueryBuilder
                .builder(DataContractNames.TABLE_NAME)
                .orderBy(getSortColumn(sortBy));
        if ((status != null) && (!status.isEmpty())) {
            queryBuilder.selection(DataContractNames.COL_STATUS, new String[]{status});
        }
        return new SimpleSQLiteQuery(queryBuilder.create().getSql());
    }

    /**
     * Get a column name to sort
     */
    private static String getSortColumn(String value) {
        switch (value) {
            case "DATE":
                return DataContractNames.COL_DATE;
            case "CHILD_NAME":
                return DataContractNames.COL_CHILD_NAME;
            case "CHILD_SURNAME":
                return DataContractNames.COL_CHILD_SURNAME;
            case "MEDICAL":
                return DataContractNames.COL_MEDICAL;
            case "STATUS":
                return DataContractNames.COL_STATUS;
            default:
                return DataContractNames.COL_DATE;
        }
    }

}
