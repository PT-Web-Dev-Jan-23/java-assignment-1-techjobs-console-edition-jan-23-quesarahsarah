import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();
        ArrayList<String> values = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }
        // Bonus mission: sort the results
        Collections.sort(values);
        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {
        loadData();  // load data, if not already loaded
        // Bonus mission; normal version returns allJobs
        return new ArrayList<>(allJobs);
    }
    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {
        loadData(); // load data, if not already loaded
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        //for listing in Arraylist, get value (skill, location, etc)
        for (HashMap<String, String> listing : allJobs) {
            String aValue = listing.get(column);
            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(listing);
            }
        }
        return jobs;
    }
    /*Search all columns for the given term
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        loadData(); // load data, if not already loaded
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        // for listing in arraylist, if listing contains value, add listing to jobs
        for (HashMap<String, String> listing : allJobs){
            Set<String> akeys = new HashSet<String>();
            akeys = listing.keySet();
            for (String key : akeys){
                String aValue = listing.get(key);
                if (aValue.toLowerCase().contains(value.toLowerCase())&& !jobs.contains(listing)){
                    jobs.add(listing);
                }
            }
        }
        // TODO - implement this method
        return jobs;
    }
    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
