package com.pujoy.charminder.other;

import java.util.ArrayList;
import java.util.Calendar;

public class SpeechParser {
	ArrayList<String> mSpeechText;
	int iLang;
	public String sTimePhrase;

	public SpeechParser(ArrayList<String> speechText, int language) {
		mSpeechText = speechText;
		iLang = language;
	}

	static public class ParseResult {
		public String sTimePhrase;
		public Calendar mCalendar;
		public int iHit;
	}

	public SpeechParser(ArrayList<String> speechText) {
		this(speechText, 0);
	}

	public void setLanguage(int language) {
		iLang = language;
	}

	public void setSpeechText(ArrayList<String> speechText) {
		mSpeechText = speechText;
	}

	public ParseResult parse() {
		ParseResult[] parseResult = new ParseResult[mSpeechText.size()];
		for (int i = 0; i < mSpeechText.size(); i++) {
			switch (iLang) {
			case 0:
				parseResult[i] = parseChinese(mSpeechText.get(i));
				break;
			case 1:
				parseResult[i] = parseChinese(mSpeechText.get(i));
				break;
			}
		}
		// Compare results and get the most reliable one
		int maxHit = 0;
		for (int i = 0; i <= (parseResult.length + 1) / 2 - 1; i++) {
			for (int j = 0; j < parseResult.length; j++) {
				if (parseResult[i].sTimePhrase
						.compareTo(parseResult[j].sTimePhrase) == 0) {
					parseResult[i].iHit++;
					if (parseResult[i].iHit > maxHit) {
						maxHit = parseResult[i].iHit;
					}
				}
			}
		}
		for (int i = 0; i <= (parseResult.length + 1) / 2 - 1; i++) {
			if (parseResult[i].iHit == maxHit) {
				return parseResult[i];
			}
		}

		return null;
	}

	private static boolean arrayContain(char[] array, char element) {
		for (int i = 0; i < array.length; i++) {
			if (element == array[i]) {
				return true;
			}
		}
		return false;
	}

	private ParseResult parseChinese(String speechText) {
		ParseResult r = new ParseResult();
		Calendar cal = Calendar.getInstance();
		final char[] arabicNumbers = new char[] { '0', '1', '2', '3', '4', '5',
				'6', '7', '8', '9' };
		final char[] chineseNumbers = new char[] { '��', 'һ', '��', '��', '��',
				'��', '��', '��', '��', '��' };
		final char CHINESE_TEN = 'ʮ';
		boolean isTimeSet = false;
		boolean isDateSet = false;
		String timePhrase_time = new String();
		String timePhrase_am_pm = new String();
		String timePhrase_date = new String();
		String timePhrase_week = new String();
		String timePhrase_prefix = new String();
		int index = speechText.indexOf(CHINESE_TEN);
		while (index != -1) {
			boolean afterNumber = false;
			boolean beforeNumber = false;
			if (index >= 1) {
				afterNumber = (arrayContain(chineseNumbers,
						speechText.charAt(index - 1)));
			}
			if (index + 1 < speechText.length()) {
				beforeNumber = (arrayContain(chineseNumbers,
						speechText.charAt(index + 1)));
			}
			speechText = speechText.replaceFirst(String.valueOf(CHINESE_TEN),
					afterNumber ? beforeNumber ? "" : "0" : beforeNumber ? "1"
							: "10");

			index = speechText.indexOf(CHINESE_TEN);
		}
		for (int i = 0; i < arabicNumbers.length; i++) {
			speechText = speechText
					.replace(chineseNumbers[i], arabicNumbers[i]);
		}

		speechText = speechText.replace("�¸�����", "����");
		speechText = speechText.replace("������", "����0");
		speechText = speechText.replace("������", "����0");
		speechText = speechText.replace("��", "��");
		speechText = speechText.replace("����", "��");
		speechText = speechText.replace("���", "��30");
		speechText = speechText.replace("���Сʱ", "30����");
		speechText = speechText.replace("��Сʱ", "30����");
		speechText = speechText.replace("��Сʱ��", "Сʱ��");
		speechText = speechText.replace("Сʱ֮��", "Сʱ��");
		speechText = speechText.replace("����֮��", "���Ӻ�");

		final char CHINESE_DIAN = '��';
		index = speechText.indexOf(CHINESE_DIAN);
		while (index != -1) {
			boolean afterNumber = false;
			boolean beforeNumber = false;

			if (index >= 1) {
				afterNumber = (Character.isDigit(speechText.charAt(index - 1)));
			}
			if (index + 1 < speechText.length()) {
				if (speechText.charAt(index + 1) == '��') {
					beforeNumber = (Character.isDigit(speechText
							.charAt(index + 2)));
				} else {
					beforeNumber = (Character.isDigit(speechText
							.charAt(index + 1)));
				}
			}
			if (afterNumber) {
				if (beforeNumber) {
					int start = speechText.charAt(index + 1) == '��' ? index + 2
							: index + 1;
					int i = start;
					while (i < speechText.length()
							&& Character.isDigit(speechText.charAt(i))) {
						i++;
					}
					try {
						cal.set(Calendar.MINUTE,
								Integer.valueOf(speechText.substring(start, i)));
					} catch (Exception e) {
						Log.w(e.getMessage());
					} finally {
					}
				}
				int i = index - 1;
				while (i >= 0 && Character.isDigit(speechText.charAt(i))) {
					i--;
				}
				try {
					cal.set(Calendar.HOUR,
							Integer.valueOf(speechText.substring(i + 1, index)));
					isTimeSet = true;
					timePhrase_time = cal.get(Calendar.HOUR) + ":"
							+ cal.get(Calendar.MINUTE);
				} catch (Exception e) {
					Log.w(e.getMessage());
				} finally {
				}
				break;
			}
			index = speechText.indexOf(CHINESE_DIAN, index + 1);
		}

		index = speechText.indexOf('��');
		while (index != -1) {
			boolean afterNumber = false;
			boolean beforeNumber = false;

			if (index >= 1) {
				afterNumber = (Character.isDigit(speechText.charAt(index - 1)));
			}
			if (index + 1 < speechText.length()) {
				beforeNumber = (Character.isDigit(speechText.charAt(index + 1)));
			}
			if (afterNumber) {
				if (beforeNumber) {
					int start = index + 1;
					int i = start;
					while (i < speechText.length()
							&& Character.isDigit(speechText.charAt(i))) {
						i++;
					}
					try {
						cal.set(Calendar.DAY_OF_MONTH,
								Integer.valueOf(speechText.substring(start, i)));
					} catch (Exception e) {
						Log.w(e.getMessage());
					} finally {
					}
				}
				int i = index - 1;
				while (i >= 0 && Character.isDigit(speechText.charAt(i))) {
					i--;
				}
				try {
					cal.set(Calendar.MONTH,
							Integer.valueOf(speechText.substring(i + 1, index)) - 1);
					isDateSet = true;
					timePhrase_date = (cal.get(Calendar.MONTH) + 1) + "��"
							+ cal.get(Calendar.DAY_OF_MONTH) + "��";
				} catch (Exception e) {
					Log.w(e.getMessage());
				} finally {
				}
				break;
			}
			index = speechText.indexOf('��', index + 1);
		}

		if (!isDateSet) {
			index = speechText.indexOf('��');
			while (index != -1) {
				boolean afterNumber = false;
				if (index >= 1) {
					afterNumber = (Character.isDigit(speechText
							.charAt(index - 1)));
				}
				if (afterNumber) {
					int i = index - 1;
					while (i >= 0 && Character.isDigit(speechText.charAt(i))) {
						i--;
					}
					try {
						cal.set(Calendar.DAY_OF_MONTH, Integer
								.valueOf(speechText.substring(i + 1, index)));
						isDateSet = true;
						timePhrase_date = cal.get(Calendar.DAY_OF_MONTH) + "��";
					} catch (Exception e) {
						Log.w(e.getMessage());
					} finally {
					}
					break;
				}
				index = speechText.indexOf('��', index + 1);
			}
		}

		index = speechText.indexOf('��');
		while (index != -1) {
			if (index + 1 < speechText.length()
					&& Character.isDigit(speechText.charAt(index + 1))) {
				int start = index + 1;
				int i = start;
				while (i < speechText.length()
						&& Character.isDigit(speechText.charAt(i))) {
					i++;
				}
				try {
					int j = Integer.valueOf(speechText.substring(start, i));
					cal.set(Calendar.DAY_OF_WEEK, j + 1);
					isDateSet = true;
					if (j == 0) {
						timePhrase_week = "����";
					} else {
						timePhrase_week = "��"
								+ chineseNumbers[cal.get(Calendar.DAY_OF_WEEK) - 1];
					}
				} catch (Exception e) {
					Log.w(e.getMessage());
				} finally {
				}
				break;
			}
			index = speechText.indexOf('��', index + 1);
		}

		if (speechText.indexOf("�����") != -1) {
			cal.add(Calendar.DAY_OF_MONTH, 3);
			timePhrase_prefix = "�����";
		} else if (speechText.indexOf("����") != -1) {
			cal.add(Calendar.DAY_OF_MONTH, 2);
			timePhrase_prefix = "����";
		} else if (speechText.indexOf("����") != -1) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
			timePhrase_prefix = "����";
		} else if (speechText.indexOf("����") != -1) {
			cal.add(Calendar.WEEK_OF_MONTH, 1);
			timePhrase_prefix = "����";
		} else if (speechText.indexOf("�¸���") != -1) {
			cal.add(Calendar.MONTH, 1);
			timePhrase_prefix = "�¸���";
		} else if (speechText.indexOf("����") != -1) {
			cal.add(Calendar.YEAR, 1);
			timePhrase_prefix = "����";
		} else {
			final String[] timeUnit = new String[] { "����", "Сʱ", "��", "��",
					"����", "��" };
			final int[] timeUnitField = new int[] { Calendar.MINUTE,
					Calendar.HOUR, Calendar.DAY_OF_MONTH,
					Calendar.WEEK_OF_MONTH, Calendar.MONTH, Calendar.YEAR };
			for (int i = 0; i < timeUnit.length; i++) {
				if (isTimeSet == true && i < 2)
					continue;
				index = speechText.indexOf(timeUnit[i] + "��");
				if (index >= 1
						&& Character.isDigit(speechText.charAt(index - 1))) {
					int start = index - 1;
					while (start >= 0
							&& Character.isDigit(speechText.charAt(start))) {
						start--;
					}
					try {
						int j = Integer.valueOf(speechText.substring(start + 1,
								index));
						cal.add(timeUnitField[i], j);
						timePhrase_prefix = j + timeUnit[i] + "��";
						break;
					} catch (Exception e) {
						Log.w(e.getMessage());
					} finally {
					}

				}
			}

		}

		if (speechText.indexOf("����") != -1) {
			timePhrase_am_pm = "����";
			cal.set(Calendar.AM_PM, Calendar.AM);
		} else if (speechText.indexOf("����") != -1) {
			timePhrase_am_pm = "����";
			cal.set(Calendar.AM_PM, Calendar.PM);
		} else if (speechText.indexOf("����") != -1) {
			timePhrase_am_pm = "����";
			cal.set(Calendar.AM_PM, Calendar.AM);
		} else if (speechText.indexOf("����") != -1) {
			timePhrase_am_pm = "����";
			cal.set(Calendar.AM_PM, Calendar.PM);
		}

		r.mCalendar = cal;
		r.sTimePhrase = timePhrase_prefix + timePhrase_date + timePhrase_week
				+ timePhrase_am_pm + timePhrase_time;
		if (r.sTimePhrase == "") {
			r.sTimePhrase = "δ��ʶ��ʱ��";
		}
		return r;
	}

	private void parseEnglish(String speechText) {
		Calendar cal = Calendar.getInstance();

	}

}
