package org.sic4change.nut4health.data;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;

import org.sic4change.nut4health.data.names.DataContractNames;
import org.sic4change.nut4health.data.names.DataRankingNames;

public class SortUtils {

    /**
     * A raw query at runtime to oder by column for getting all the contracts sorted.
     * @param sortBy
     * @param status
     * @return
     */
    public static SimpleSQLiteQuery getAllQueryContracts(String sortBy, String status) {
        /*SupportSQLiteQueryBuilder queryBuilder = SupportSQLiteQueryBuilder
                .builder(DataContractNames.TABLE_NAME)
                .orderBy(getSortColumn(sortBy));
        if ((status != null) && (!status.isEmpty())) {
            queryBuilder.selection(DataContractNames.COL_STATUS, new String[]{status});
        }
        return new SimpleSQLiteQuery(queryBuilder.create().getSql());*/
        return new SimpleSQLiteQuery("SELECT * FROM " + DataContractNames.TABLE_NAME + " ORDER BY " + DataContractNames.COL_DATE
            +  " DESC");
    }

    /**
     * A raw query at runtime to oder by column for getting all the ranking sorted.
     * @param sortBy
     * @return
     */
    public static SimpleSQLiteQuery getAllQueryRanking(String sortBy) {
        /*SupportSQLiteQueryBuilder queryBuilder = SupportSQLiteQueryBuilder
                .builder(DataContractNames.TABLE_NAME)
                .orderBy(getSortColumn(sortBy));
        if ((status != null) && (!status.isEmpty())) {
            queryBuilder.selection(DataContractNames.COL_STATUS, new String[]{status});
        }
        return new SimpleSQLiteQuery(queryBuilder.create().getSql());*/
        return new SimpleSQLiteQuery("SELECT * FROM " + DataRankingNames.TABLE_NAME + " WHERE " +  DataRankingNames.COL_USERNAME + " "
                + " NOT LIKE 'anonymous%'"
                + " ORDER BY " + DataRankingNames.COL_POINTS
                +  " DESC");
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
            case "POINTS":
                return DataRankingNames.COL_POINTS;
            default:
                return DataContractNames.COL_DATE;
        }
    }

}
