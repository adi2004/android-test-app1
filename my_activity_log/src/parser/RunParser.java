package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunParser
{
	public static void main(String[] args) throws IOException, ParseException {
		Config config = new Config();

		int idle_count = 0;
		int work_count = 0;
		int procratinating_count = 0;
		int total_idle_count = 0;
		int total_work_count = 0;
		int total_procratinating_count = 0;
		String file = config.getFile();
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String line;
		Date previousDate = null;
		Date currentDate = null;
		while ((line = br.readLine()) != null) {
			String dateString = line.split("-")[0];
			SimpleDateFormat ft = new SimpleDateFormat ("hh:mm dd MMMMM yyyy");

			currentDate = ft.parse(dateString);
			if (previousDate == null)
				previousDate = currentDate;
			if (currentDate.getDay() != previousDate.getDay()) {
				System.out.printf("For date %s here are the results: \n", previousDate.toLocaleString());
				System.out.printf("\tNumber of %s occurences = %d, that's %d h\n",config.getIdle()[0], idle_count, idle_count/60);
				System.out.printf("\tNumber of %s occurences = %d, that's %d h\n",config.getProcrastinating()[0], procratinating_count, procratinating_count/60);
				System.out.printf("\tNumber of %s occurences = %d, that's %d h\n",config.getWork()[0], work_count, work_count/60);
				if (work_count/60 >= 5)
					System.out.printf("!!! Wow !!!!!! Wow !!!!!! Wow !!!!!! Wow !!!!!! Wow !!! %f\n", work_count/60.);

				if (procratinating_count/60 >= 1)
					System.out.printf("!!! HMMMMM !!!!!! HMMMMM !!!!!! HMMMMM !!!!!! HMMMMM !!!!!! HMMMMM !!! %f\n", procratinating_count/60.);

				previousDate = currentDate;
				total_idle_count += idle_count;
				total_work_count += work_count;
				total_procratinating_count += procratinating_count;
				idle_count = 0;
				work_count = 0;
				procratinating_count = 0;
			}

			Boolean stop = false;
			for (String word:config.getIdle())
			{
				if (line.matches(".*"+word+".*")){ idle_count ++; stop = true; break; }
			}
			if (!stop)
			for (String word:config.getProcrastinating())
			{
				if (line.matches(".*"+word+".*")){ procratinating_count ++; stop = true; break; }
			}
			if (!stop)
			for (String word:config.getWork())
			{
				if (line.matches(".*"+word+".*")){ work_count ++; break; }
			}
		}

		System.out.printf("For date %s here are the results: \n", currentDate.toLocaleString());
		System.out.printf("\tNumber of %s occurences = %d, that's %d h\n",config.getIdle()[0], idle_count, idle_count/60);
		System.out.printf("\tNumber of %s occurences = %d, that's %d h\n",config.getProcrastinating()[0], procratinating_count, procratinating_count/60);
		System.out.printf("\tNumber of %s occurences = %d, that's %d h\n",config.getWork()[0], work_count, work_count/60);


		System.out.printf("Total work is %.2f hours or %.2f days;\n", total_work_count / 60., total_work_count / 1440.);
		System.out.printf("Total idle is %.2f hours or %.2f days;\n", total_idle_count / 60., total_idle_count / 1440.);
		System.out.printf("Total procrastinating is %.2f hours or %.2f days;\n", total_procratinating_count / 60., total_procratinating_count / 1440.);

		br.close();
	}
}
