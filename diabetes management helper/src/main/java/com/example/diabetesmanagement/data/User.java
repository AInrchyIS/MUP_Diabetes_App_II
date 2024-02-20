package com.example.diabetesmanagement.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class User implements Serializable {

    //private String fName;
    //private String lName;
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    private static final String TAG = "User";

    private String userName;

    private int reminderRequestCodes;


    /**
     * reminderRequestCodes is the request code for each notification, so there aren't any conflicts.
     * 0 and 1 are reserved for notification publishing.
     * 2 and 3 are for preset notification schedulers.
     * 4 & 5 are used for notifications when daily and weekly goals are met.
     * 7 is for purging old logs from user files and moving to data storage
     * 10 & 11 are for sending morning messages.
     * Everything after 100 is for user added reminders, and is handled in app.
     */
    private String id;
    private int age;
    private int weight;
    private String diabetesType;
    int totalPoints;
    //private int targetRangeLower;
    //private int targetRangeUpper;
    private int currentBloodSugar;
    private String gender;
    private boolean textToSpeechEnabled;
    private int totalScore, totalBadges;
    private boolean Today, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
    private Date goalReset;
    //private Date nextPurge;

    private ArrayList<Reminder> reminders = new ArrayList<>();
    private ArrayList<Goal> goals = new ArrayList<>();
    private ArrayList<Medication> medications = new ArrayList<>();

    private ArrayList<LogBloodSugar> logsBloodSugar = new ArrayList<>();
    private ArrayList<LogInsulin> logsInsulin = new ArrayList<>();
    private ArrayList<LogFood> logsFood = new ArrayList<>();
    private ArrayList<LogMedication> logsMedication = new ArrayList<>();
    private ArrayList<LogActivity> logsActivity = new ArrayList<>();
    private ArrayList<LogWeight> logsWeight = new ArrayList<>();
    private ArrayList<LogMood> logsMood = new ArrayList<>();

    //TODO: denormalize logs, and possibly goals/reminders/medications
    public User() {

    }

    public User(String userId) {
        //setfName("");
        //setlName("");
        setId(userId);
        setUserName("");
        setReminderRequestCodes(100); //leave a buffer for non-user made PendingIntents, users start at 100 and increment by 7 (7 needed for weekly reminders)
        setAge(0);
        setWeight(0);
        setDiabetesType("");
        //setTargetRangeLower(0);
        //setTargetRangeUpper(0);
        setCurrentBloodSugar(0);
        setTextToSpeechEnabled(true);
        initializeGoals();
        initializeScores();
        //initializePurgeDate();
    }

    /*
    public void setNextPurge(Date date){
        nextPurge = date;
    }

    public Date getNextPurge(){
        return nextPurge;
    }

    public void initializePurgeDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 3);
        c.set(Calendar.MINUTE, (int)(Math.floor(Math.random()*59)));
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
            c.add(Calendar.DATE, 1);
        }
        setNextPurge(c.getTime());
    }
    */
    public void addMedicationGoal(Medication medication) {
        Date firstDailyReset;
        Date firstWeeklyReset;

        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0);
        c.add(Calendar.DATE, 1);
        firstDailyReset = c.getTime();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            c.add(Calendar.DATE, 1);
        firstWeeklyReset = c.getTime();

        Goal medicationGoal = new Goal();
        medicationGoal.setDailyAmount(medication.getDailyDoses());
        medicationGoal.setWeeklyAmount(7);
        medicationGoal.setType("Medication: " + medication.getName());
        medicationGoal.setUserID(id);
        medicationGoal.setNextDailyReset(firstDailyReset);
        medicationGoal.setNextWeeklyReset(firstWeeklyReset);
        addGoal(medicationGoal);
    }

    private void initializeGoals() {
        //initialize default goals, for this initial experiment will only use preset goals to simplify implementation.
        // Values for goal targets can be changed by the user during setup/registration, and during normal app usage
        //setup minutes of activity/exercise

        Date firstDailyReset;
        Date firstWeeklyReset;

        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0);
        c.add(Calendar.DATE, 1);
        firstDailyReset = c.getTime();
        android.util.Log.d("TIMEDEBUG", "Daily reset time: " + c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH)
                + " " + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            c.add(Calendar.DATE, 1);
        firstWeeklyReset = c.getTime();
        android.util.Log.d("TIMEDEBUG", "Weekly reset time: " + c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH)
                + " " + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));


        Goal activity = new Goal();
        activity.setDailyAmount(30);
        activity.setWeeklyAmount(120);
        activity.setType(Goal.ACTIVITY);
        activity.setUserID(id);
        activity.setNextDailyReset(firstDailyReset);
        activity.setNextWeeklyReset(firstWeeklyReset);
        addGoal(activity);
        //setup # of log entries per day/week for important types
        Goal bloodSugar = new Goal();
        bloodSugar.setDailyAmount(3);
        bloodSugar.setWeeklyAmount(14);
        bloodSugar.setType(Goal.BLOODSUGAR);
        bloodSugar.setUserID(id);
        bloodSugar.setNextDailyReset(firstDailyReset);
        bloodSugar.setNextWeeklyReset(firstWeeklyReset);
        addGoal(bloodSugar);

        Goal insulin = new Goal();
        insulin.setDailyAmount(1);
        insulin.setWeeklyAmount(7);
        insulin.setType(Goal.INSULIN);
        insulin.setUserID(id);
        insulin.setNextDailyReset(firstDailyReset);
        insulin.setNextWeeklyReset(firstWeeklyReset);
        addGoal(insulin);
        /**For Medication*/
        /*Goal medicine = new Goal();
        // targets are 0 by default, means it won't be used for feedback purposes.  Can be changed if user has medications
        medicine.setType(Goal.MEDICINE);
        medicine.setDailyAmount(0);
        medicine.setWeeklyAmount(0);
        //medicine.setActive(false);
        medicine.setUserID(id);
        medicine.setNextDailyReset(firstDailyReset);
        medicine.setNextWeeklyReset(firstWeeklyReset);
        addGoal(medicine);*/
        /**For Food Meal*/
        Goal foodmeal = new Goal();
        foodmeal.setMealAmount(3);
        foodmeal.setType(Goal.FOODMEAL);
        foodmeal.setUserID(id);
        foodmeal.setWeeklyAmount(7);
        foodmeal.setNextDailyReset(firstDailyReset);
        foodmeal.setNextWeeklyReset(firstWeeklyReset);
        addGoal(foodmeal);

        Goal foodsnack = new Goal();
        foodsnack.setSnackAmount(2);
        foodsnack.setType(Goal.FOODSNACK);
        foodsnack.setUserID(id);
        foodsnack.setWeeklyAmount(7);
        foodsnack.setNextDailyReset(firstDailyReset);
        foodsnack.setNextWeeklyReset(firstWeeklyReset);
        addGoal(foodsnack);

        Goal weight = new Goal();
        // start at weighing once a week, weighing once a day might be difficult/usually has too much fluctuation
        weight.setDailyAmount(1);
        weight.setWeeklyAmount(1);
        weight.setType(Goal.WEIGHT);
        weight.setUserID(id);
        weight.setNextDailyReset(firstDailyReset);
        weight.setNextWeeklyReset(firstWeeklyReset);
        addGoal(weight);

        Goal mood = new Goal();
        mood.setDailyAmount(1);
        mood.setWeeklyAmount(7);
        mood.setType(Goal.MOOD);
        mood.setUserID(id);
        mood.setNextDailyReset(firstDailyReset);
        mood.setNextWeeklyReset(firstWeeklyReset);
        addGoal(mood);
    }

    private void initializeScores() {
        Date goalReset;
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0);
        c.add(Calendar.DATE, 1);

        goalReset = c.getTime();
        setToday(false);
        setBadges(0);
        setPoints(0);
        setGoalReset(goalReset);
    }

    public void setGoalReset(Date goalReset) {
        this.goalReset = goalReset;
    }

    public Date getGoalReset() {
        return goalReset;
    }

    public void setPoints(int totalScore) {
        this.totalPoints = totalScore;
    }

    public int getPoints() {
        return totalPoints;
    }

    public void setBadges(int numberOfBadgesEarned) {
        this.totalBadges = numberOfBadgesEarned;
    }

    public int getBadges() {
        return totalBadges;
    }

    public void setSunday(boolean b) {
        this.Sunday = b;
    }

    public void setMonday(boolean b) {
        this.Monday = b;
    }

    public void setTuesday(boolean b) {
        this.Tuesday = b;
    }

    public void setWednesday(boolean b) {
        this.Wednesday = b;
    }

    public void setThursday(boolean b) {
        this.Thursday = b;
    }

    public void setFriday(boolean b) {
        this.Friday = b;
    }

    public void setSaturday(boolean b) {
        this.Saturday = b;
    }

    public void setToday(boolean b) {
        this.Today = b;
    }


    public boolean getSunday() {
        return this.Sunday;
    }

    public boolean getMonday() {
        return this.Monday;
    }

    public boolean getTuesday() {
        return this.Tuesday;
    }

    public boolean getWednesday() {
        return this.Wednesday;
    }

    public boolean getThursday() {
        return this.Thursday;
    }

    public boolean getFriday() {
        return this.Friday;
    }

    public boolean getSaturday() {
        return this.Saturday;
    }

    public boolean getToday() {
        return this.Today;
    }


    public boolean isTextToSpeechEnabled() {
        return textToSpeechEnabled;
    }

    public void setTextToSpeechEnabled(boolean textToSpeechEnabled) {
        this.textToSpeechEnabled = textToSpeechEnabled;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getReminderRequestCodes() {
        return reminderRequestCodes;
    }

    public void setReminderRequestCodes(int reminderRequestCodes) {
        this.reminderRequestCodes = reminderRequestCodes;
    }

    public double weeklyA1C() {
        return (46.7 + weeklyAvgBloodSugar()) / 28.7;
    }

    public double monthlyA1C() {
        return (46.7 + monthlyAvgBloodSugar()) / 28.7;
    }

    /*public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
    */

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    public String getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(String diabetesType) {
        this.diabetesType = diabetesType;
    }

    /*
        public int getTargetRangeLower() {
            return targetRangeLower;
        }

        public void setTargetRangeLower(int targetRangeLower) {
            this.targetRangeLower = targetRangeLower;
        }

        public int getTargetRangeUpper() {
            return targetRangeUpper;
        }

        public void setTargetRangeUpper(int targetRangeUpper) {
            this.targetRangeUpper = targetRangeUpper;
        }
        */
    public int getCurrentBloodSugar() {
        return currentBloodSugar;
    }

    public void setCurrentBloodSugar(int currentBloodSugar) {
        this.currentBloodSugar = currentBloodSugar;
    }


    public int weeklyAvgBloodSugar() {
        ArrayList<LogBloodSugar> weeklyLogs = new ArrayList<>();

        ArrayList<LogBloodSugar> logs = getLogsBloodSugar();
        //get this week's logs
        Calendar start = Calendar.getInstance();
        start.roll(Calendar.WEEK_OF_YEAR, -1);
        android.util.Log.d("DEBUGGING", "Start Year/week: " + start.get(Calendar.YEAR) + "/" + start.get(Calendar.WEEK_OF_YEAR));
        Calendar end = Calendar.getInstance();
        android.util.Log.d("DEBUGGING", "End Year/week: " + end.get(Calendar.YEAR) + "/" + end.get(Calendar.WEEK_OF_YEAR));

        for (LogBloodSugar log : logs) {
            if (start.getTimeInMillis() <= log.getDateTime().getTime() && log.getDateTime().getTime() <= end.getTimeInMillis())
                weeklyLogs.add(log);
        }
        int totalBloodSugar = 0;
        int numEntries = 0;
        for (LogBloodSugar log : weeklyLogs) {
            numEntries++;
            totalBloodSugar += log.getBloodSugar();
        }
        return Math.round(((float) totalBloodSugar) / numEntries);
    }


    public int monthlyAvgBloodSugar() {

        ArrayList<LogBloodSugar> monthlyLogs = new ArrayList<>();

        ArrayList<LogBloodSugar> logs = getLogsBloodSugar();

        Calendar start = Calendar.getInstance();
        start.roll(Calendar.MONTH, -1);
        android.util.Log.d("DEBUGGING", "Start Year/Month: " + start.get(Calendar.YEAR) + "/" + start.get(Calendar.WEEK_OF_YEAR));
        Calendar end = Calendar.getInstance();
        android.util.Log.d("DEBUGGING", "End Year/Month: " + end.get(Calendar.YEAR) + "/" + end.get(Calendar.WEEK_OF_YEAR));

        for (LogBloodSugar log : logs) {
            if (start.getTimeInMillis() <= log.getDateTime().getTime() && log.getDateTime().getTime() <= end.getTimeInMillis())
                monthlyLogs.add(log);
        }

        int totalBloodSugar = 0;
        int numEntries = 0;
        for (LogBloodSugar log : monthlyLogs) {
            numEntries++;
            totalBloodSugar += log.getBloodSugar();
        }
        return Math.round(((float) totalBloodSugar) / numEntries);
    }

    /*
    public ArrayList<ArrayList<Log>> logs()
    {

        ArrayList<ArrayList<Log>> logs = new ArrayList<>();
        logs.add(getLogsBloodSugar());
        logs.add(getLogsInsulin());
        logs.add(getLogsFood());
        logs.add(getLogsMedication());
        logs.add(getLogsActivity());
        logs.add(getLogsWeight());
        logs.add(getLogsMood());
        return logs;
    }
    */
    public void addLogBloodSugar(LogBloodSugar l) {
        logsBloodSugar.add(l);
    }

    public void addLogInsulin(LogInsulin l) {
        logsInsulin.add(l);
    }


    public void addLogMedication(LogMedication l) {
        logsMedication.add(l);
    }

    public void addLogFood(LogFood l) {
        logsFood.add(l);
    }

    public void addLogActivity(LogActivity l) {
        logsActivity.add(l);
    }

    public void addLogWeight(LogWeight l) {
        logsWeight.add(l);
    }

    public void addLogMood(LogMood l) {
        logsMood.add(l);
    }

    /*
        public void addLog(Log l)
        {
            if (l != null){
                switch (l.logType())
                {
                    case "BloodSugar": logsBloodSugar.add(l);
                        break;
                    case "Insulin": logsInsulin.add(l);
                        break;
                    case "Food": logsFood.add(l);
                        break;
                    case "Medication": logsMedication.add(l);
                        break;
                    case "Activity": logsActivity.add(l);
                        break;
                    case "Weight": logsWeight.add(l);
                        break;
                    case "Mood": logsMood.add(l);
                        break;
                }
            }
        }
    */
    public boolean hasMedication() {
        return (medications.size() > 0);
    }

    public Medication getMedication(String name) {
        for (Medication m : getMedications()) {
            if (m.getName().equals(name))
                return m;
        }
        Medication none = new Medication();
        none.setName("None");
        return none;
    }

    public void addMedication(Medication m) {
        medications.add(m);
    }

    public void addMedication(Medication m, int i) {
        medications.add(i, m);
    }


    public int removeMedication(String s) {
        int index = -1;
        for (int i = 0; i < medications.size(); i++) {
            if (medications.get(i).getName().equals(s))
                index = i;
        }
        if (index != -1)
            medications.remove(index);
        return index;
    }


    public void addReminder(Reminder r) {
        reminders.add(r);
    }

    public void addReminder(Reminder r, int i) {
        reminders.add(i, r);
    }

    public int removeReminder(String s) {
        int index = -1;
        for (int i = 0; i < reminders.size(); i++) {
            if (reminders.get(i).getDescription().equals(s))
                index = i;
        }
        if (index != -1)
            reminders.remove(index);
        return index;
    }


    public void addGoal(Goal g) {
        goals.add(g);
    }

    public void addGoal(Goal g, int i) {
        goals.add(i, g);
    }

    public int removeGoal(String s) {
        int index = -1;
        for (int i = 0; i < goals.size(); i++) {
            if (goals.get(i).getType().equals(s))
                index = i;
        }
        if (index != -1)
            goals.remove(index);
        return index;
    }


    public ArrayList<LogBloodSugar> getLogsBloodSugar() {
        //return new ArrayList<Log>();
        return logsBloodSugar;
    }

    public ArrayList<LogInsulin> getLogsInsulin() {
        //return new ArrayList<>();
        return logsInsulin;
    }

    public ArrayList<LogFood> getLogsFood() {
        //return new ArrayList<>();
        return logsFood;
    }

    public ArrayList<LogMedication> getLogsMedication() {
        //return new ArrayList<>();
        return logsMedication;
    }

    public ArrayList<LogActivity> getLogsActivity() {
        //return new ArrayList<>();
        return logsActivity;
    }

    public ArrayList<LogWeight> getLogsWeight() {
        //return new ArrayList<>();
        return logsWeight;
    }

    public ArrayList<LogMood> getLogsMood() {
        //return new ArrayList<>();
        return logsMood;
    }

    public void setLogsBloodSugar(ArrayList<LogBloodSugar> logsBloodSugar) {
        this.logsBloodSugar = logsBloodSugar;
    }

    public void setLogsInsulin(ArrayList<LogInsulin> logsInsulin) {
        this.logsInsulin = logsInsulin;
    }

    public void setLogsFood(ArrayList<LogFood> logsFood) {
        this.logsFood = logsFood;
    }

    public void setLogsMedication(ArrayList<LogMedication> logsMedication) {
        this.logsMedication = logsMedication;
    }

    public void setLogsActivity(ArrayList<LogActivity> logsActivity) {
        this.logsActivity = logsActivity;
    }

    public void setLogsWeight(ArrayList<LogWeight> logsWeight) {
        this.logsWeight = logsWeight;
    }

    public void setLogsMood(ArrayList<LogMood> logsMood) {
        this.logsMood = logsMood;
    }

    public ArrayList<Medication> getMedications() {
        //return new ArrayList<>();
        return medications;
    }

    public ArrayList<Reminder> getReminders() {
        //return new ArrayList<>();
        return reminders;
    }

    public ArrayList<Goal> getGoals() {
        //return new ArrayList<>();
        return goals;
    }


    public Goal getGoal(String type) {
        Goal goal = new Goal();
        for (Goal g : getGoals()) {
            if (g.getType().equals(type))
                goal = g;
        }
        return goal;
    }


    public int reminderCount(String s) {
        int i = 0;
        for (Reminder r : getReminders()) {
            if (r.getDescription().equals(s))
                i++;
        }
        return i;
    }


    public int medicationCount(String s) {
        int i = 0;
        for (Medication m : medications) {
            if (m.getName().equals(s))
                i++;
        }
        return i;
    }


    public String toString() {
        String user = "";
        String[] s = {"Blood sugar", "Insulin", "Food", "Medication", "Activity", "Weight", "Mood"};
        //user+=" Name: ";
        //user+=getfName() + " " + getlName();
        user += " ID: " + getId();
        user += " Age: " + getAge();
        user += " Gender: " + getGender();
        user += " Weight: " + getWeight();
        user += " Diabetes type: " + getDiabetesType();
        /*
        user+=" \n Logs entered: \n";
         ArrayList<ArrayList<Log>> logs = logs();
        int i =0;
        StringBuilder logCount = new StringBuilder();
        for (ArrayList<Log> logType : logs )
        {
           logCount.append(logType.size());
           logCount.append(" ");
           logCount.append(s[i]);
           logCount.append(" Logs \n");

            i++;
            //for(Log log : logType){
            // }
        }
        user += logCount.toString();*/

        return user;
    }
}
