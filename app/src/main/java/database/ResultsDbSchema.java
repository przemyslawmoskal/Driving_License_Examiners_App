package database;

public class ResultsDbSchema {

    public static final class ResultsTable {
        public static final String NAME = "results";

        public static final class Cols {
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String DAY = "day";
            public static final String ORDER_NUMBER = "orderNumber";
            public static final String CATEGORY = "category";
            public static final String RESULT = "result";
            public static final String UUID = "uuid";
        }
    }

}
