package org.sic4change.nut4health.data;


import androidx.sqlite.db.SimpleSQLiteQuery;

import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.data.names.DataContractNames;
import org.sic4change.nut4health.data.names.DataMalnutritionChildTableNames;
import org.sic4change.nut4health.data.names.DataNearNames;
import org.sic4change.nut4health.data.names.DataNotificationNames;
import org.sic4change.nut4health.data.names.DataPaymentNames;
import org.sic4change.nut4health.data.names.DataRankingNames;

public class SortUtils {

    /**
     * A raw query at runtime to oder by column for getting filter and sorted contracts.
     * @param sortBy
     * @param name
     * @param surname
     * @param status
     * @param dateStart
     * @param dataEnd
     * @param percentageMin
     * @param percentageMax
     * @return
     */
    public static SimpleSQLiteQuery getFilterContracts(String sortBy, String name, String surname, String status, long dateStart, long dataEnd,
                                                       int percentageMin, int percentageMax) {
        String query = "SELECT * FROM " + DataContractNames.TABLE_NAME;
        if ((name != null) && (!name.isEmpty())) {
            query = query + " WHERE " + DataContractNames.COL_CHILD_NAME + " LIKE " + "'%" + name + "%'";
        }
        if ((surname != null) && (!surname.isEmpty())) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataContractNames.COL_CHILD_SURNAME + " LIKE " + "'%" + surname + "%'";
            } else {
                query = query + " WHERE " + DataContractNames.COL_CHILD_SURNAME + " LIKE " + "'%" + surname + "%'";
            }
        }
        if (!status.equals(Contract.Status.EMPTY.name())) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataContractNames.COL_STATUS + " = " + "'" + status + "'";
            } else {
                query = query + " WHERE " + DataContractNames.COL_STATUS + " = " + "'" + status + "'";
            }
        }
        if (dateStart != 0 && dataEnd != 0) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataContractNames.COL_DATE_MILI + " >= " + dateStart + " AND " + DataContractNames.COL_DATE_MILI + " <= " + dataEnd;
            } else {
                query = query + " WHERE " + DataContractNames.COL_DATE_MILI + " >= " + dateStart + " AND " + DataContractNames.COL_DATE_MILI + " <= " + dataEnd;
            }
        }
        if (query.contains("WHERE")) {
            query = query + " AND " + DataContractNames.COL_PERCENTAGE + " >= " + percentageMin + " AND " + DataContractNames.COL_PERCENTAGE + " <= " + percentageMax;
        } else {
            query = query + " WHERE " + DataContractNames.COL_PERCENTAGE + " >= " + percentageMin + " AND " + DataContractNames.COL_PERCENTAGE + " <= " + percentageMax;
        }
        return new SimpleSQLiteQuery(query + " ORDER BY " + DataContractNames.COL_DATE_MILI
            +  " DESC");
    }


    /**
     * A raw query at runtime to oder by column for getting filter and sorted near contracts.
     * @param sortBy
     * @param name
     * @param surname
     * @param status
     * @param dateStart
     * @param dataEnd
     * @param percentageMin
     * @param percentageMax
     * @return
     */
    public static SimpleSQLiteQuery getFilterNearContracts(String sortBy, String name, String surname, String status, long dateStart, long dataEnd,
                                                       int percentageMin, int percentageMax) {
        String query = "SELECT * FROM " + DataNearNames.TABLE_NAME;
        if ((name != null) && (!name.isEmpty())) {
            query = query + " WHERE " + DataNearNames.COL_CHILD_NAME + " LIKE " + "'%" + name + "%'";
        }
        if ((surname != null) && (!surname.isEmpty())) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataNearNames.COL_CHILD_SURNAME + " LIKE " + "'%" + surname + "%'";
            } else {
                query = query + " WHERE " + DataNearNames.COL_CHILD_SURNAME + " LIKE " + "'%" + surname + "%'";
            }
        }
        if (!status.equals(Near.Status.EMPTY.name())) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataNearNames.COL_STATUS + " = " + "'" + status + "'";
            } else {
                query = query + " WHERE " + DataNearNames.COL_STATUS + " = " + "'" + status + "'";
            }
        }
        if (dateStart != 0 && dataEnd != 0) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataNearNames.COL_DATE_MILI + " >= " + dateStart + " AND " + DataNearNames.COL_DATE_MILI + " <= " + dataEnd;
            } else {
                query = query + " WHERE " + DataNearNames.COL_DATE_MILI + " >= " + dateStart + " AND " + DataNearNames.COL_DATE_MILI + " <= " + dataEnd;
            }
        }
        if (query.contains("WHERE")) {
            query = query + " AND " + DataNearNames.COL_PERCENTAGE + " >= " + percentageMin + " AND " + DataNearNames.COL_PERCENTAGE + " <= " + percentageMax;
        } else {
            query = query + " WHERE " + DataNearNames.COL_PERCENTAGE + " >= " + percentageMin + " AND " + DataNearNames.COL_PERCENTAGE + " <= " + percentageMax;
        }
        return new SimpleSQLiteQuery(query + " ORDER BY " + DataNearNames.COL_DATE_MILI
                +  " DESC");
    }

    /**
     * A raw query at runtime to oder by column for getting all the ranking sorted.
     * @param sortBy
     * @param username
     * @return
     */
    public static SimpleSQLiteQuery getAllQueryRanking(String sortBy, String username) {
        String query = "SELECT * FROM " + DataRankingNames.TABLE_NAME + " WHERE " +  DataRankingNames.COL_USERNAME
                + " NOT LIKE 'anonymous%'";
        if ((username != null) && (!username.isEmpty())) {
            query = query + " AND " + DataRankingNames.COL_USERNAME + " LIKE " + "'" + username + "%'";
        }
        return new SimpleSQLiteQuery(query + " ORDER BY " + DataRankingNames.COL_POSITION +  " ASC");
    }

    public static SimpleSQLiteQuery getFilterPayments(String sortBy, String status, long dateStart, long dataEnd) {
        String query = "SELECT * FROM " + DataPaymentNames.TABLE_NAME;
        if (!status.equals(Payment.Status.ALL.name())) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataPaymentNames.COL_TYPE + " = " + "'" + status + "'";
            } else {
                query = query + " WHERE " + DataPaymentNames.COL_TYPE + " = " + "'" + status + "'";
            }
        }
        if (dateStart != 0 && dataEnd != 0) {
            if (query.contains("WHERE")) {
                query = query + " AND " + DataPaymentNames.COL_DATE + " >= " + dateStart + " AND " + DataPaymentNames.COL_DATE + " <= " + dataEnd;
            } else {
                query = query + " WHERE " + DataPaymentNames.COL_DATE + " >= " + dateStart + " AND " + DataPaymentNames.COL_DATE + " <= " + dataEnd;
            }
        }
        return new SimpleSQLiteQuery(query + " ORDER BY " + DataPaymentNames.COL_DATE_MILI +  " DESC");
    }

    public static SimpleSQLiteQuery getNotifications() {
        String query = "SELECT * FROM " + DataNotificationNames.TABLE_NAME;
        return new SimpleSQLiteQuery(query + " ORDER BY " + DataNotificationNames.COL_DATE_MILI +  " DESC");
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
