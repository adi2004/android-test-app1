package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AHKLogParser {
	private static final String REGEX = " \\| ";

	int nrOfLines = 0;
	ArrayList<Day> days = new ArrayList<Day>();

	int newDays = 0;

	public AHKLogParser(Config cfg) {
		// init values
		// attempt to open the file
		BufferedReader input;
		try {
			// open file
			input = new BufferedReader(new FileReader(cfg.file));

			// parse line by line
			String line;
			do {
				line = input.readLine();
				if (line == null)
					continue;
				nrOfLines++;
				processLine(cfg, line);
			} while (line != null);

			// close file
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// activity breakdown
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Nr of lines in log = " + nrOfLines + "\n");
		output.append("Nr of new days in log = " + newDays + "\n");
		int startHrs[] = new int[24];
		int endHrs[] = new int[24];
		for (Day day : days) {
			startHrs[day.dayStart.get(Calendar.HOUR_OF_DAY)]++;
			endHrs[day.lastTime.get(Calendar.HOUR_OF_DAY)]++;
		}
		output.append("Startin hrs: ");
		for (int i = 0; i < 23; i++) {
			output.append(i + ":" + startHrs[i] + " ");
		}
		output.append("\nClosing hrs: ");
		for (int i = 0; i < 23; i++) {
			output.append(i + ":" + endHrs[i] + " ");
		}
		// output.append(days.toString());
		return output.toString();
	}

	public void processLine(Config cfg, String line) {
		String[] info = line.split(REGEX);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(cfg.dateFormat);
		Calendar calDate = new GregorianCalendar();
		try {
			date = format.parse(info[0]);
			calDate.setTime(date);
		} catch (ParseException e) {
			Log.i("Exception parsing: \"" + info[0] + "\" on line: " + nrOfLines);
			// e.printStackTrace();
		}
		// use the date information we extracted to update the date, or create a
		// new one
		if (isNewDay(calDate)) {
			days.add(new Day(calDate, info));
			newDays++;
		} else {
			days.get(days.size() - 1).update(calDate, info);
		}
		// Calendar cal = new GregorianCalendar();
		// cal.setTime(date);
		// Log.i(cal.getDisplayName(Calendar.HOUR_OF_DAY, Calendar.LONG, new
		// Locale("en")));
	}

	private boolean isNewDay(Calendar calDate) {
		// if we don't have any days added, then we need to create a new date
		if (days.size() == 0)
			return true;

		// get the last day entry
		Day day = days.get(days.size() - 1);

		// we have a new day if
		// 1a. the previous day was different OR
		// 1b. the previous day last entry was before the day begins AND
		// 2. if the time is after a certain hour (i.e. 4 a.m.) AND
		// 3. more then X number of hours have passed since anything changed
		if (calDate.get(Calendar.HOUR_OF_DAY) > Day.BEGINING_HOUR_OF_DAY
				&& (day.lastTime.get(Calendar.DAY_OF_YEAR) < calDate.get(Calendar.DAY_OF_YEAR) || day.lastTime
						.get(Calendar.HOUR_OF_DAY) < Day.BEGINING_HOUR_OF_DAY)) {
			return true;
		}

		return false;
	}

	private class Day {

		public Calendar lastTime;
		public Calendar dayStart;
		public static final int BEGINING_HOUR_OF_DAY = 4;

		public Day(Calendar time, String[] info) {
			// TODO Auto-generated constructor stub
			dayStart = time;
			lastTime = time;
		}

		public void update(Calendar time, String[] info) {
			lastTime = time;
		}
	}
}
