package com.example.ecommerce.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelperUtils {

	private static final BigInteger BigInteger = null;
	
	public static String replaceString(Map<String,String> variables,String templateText) {
			templateText = variables.keySet().stream()
					.reduce(templateText, 
							(originalText, key) -> originalText.replaceAll("\\$\\{" + key + "\\}", 
							variables.get(key)));
			return templateText;
	}
	
	/*
	 * ArrayList to comman seperate string
	 */
	
	public static String getCSVStringFromArrayList(ArrayList<String> list) {
		return list.toString().toString().replace("[", "").replace("]", "")
	            .replace(", ", ",");
	}
	
	public static String getCSVIntegerFromArrayList(ArrayList<Integer> list) {
		return list.toString().toString().replace("[", "").replace("]", "")
	            .replace(", ", ",");
	}
	/*
	 * convert InputStream to String
	 */
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
	public static String isNullString(Object object) {
        try {
            if (object.equals(null)) {
               return "";
            } else {
                 return (String)object;
            }
        } catch (NullPointerException n) {
            return "";
        }
    }
	public static Long isNullLong(Object object) {
        try {
            if (object == null) {
               return 0l;
            } else {
                 return (long)object;
            }
        } catch (NullPointerException n) {
            return 0l;
        }
    }
	public static int isNullInt(Object object) {
        try {
            if (object == null) {
               return 0;
            } else {
                 return (int)object;
            }
        } catch (NullPointerException n) {
            return 0;
        }
    }
		
	public static Date isNullDate(Object object) 
	{
        try 
        {
        	
            if (object == null) 
            {
               return new Date();
            } 
            else 
            {
                return (Date) object;
            }
        } 
        catch (NullPointerException n)
        {
            return new Date();
        }
    }
	
	public static BigDecimal isNullBigDecimal(Object object) {
        try 
        {
            if (object == null) {
               return new BigDecimal(0);
            } else {
                 return (BigDecimal)object;
            }
        } catch (NullPointerException n) {
            return new BigDecimal(0);
        }
    }
	
	public static char isNullChar(Object object) {
        try {
            if (object == null) {
               return 'N';
            } else {
                 return (char)object;
            }
        } catch (NullPointerException n) {
            return 'N';
        }
    }
	
	public static Double isNullDouble(Object object) {
        try {
            if (object == null) {
               return 0D;
            } else {
                 return (Double)object;
            }
        } catch (NullPointerException n) {
            return 0D;
        }
    }
	public static Short isNullShort(Object object) {
        try {
            if (object == null) {
               return 0;
            } else {
                 return (Short)object;
            }
        } catch (NullPointerException n) {
            return 0;
        }
    }
	
	
	public static BigInteger isNullBigInteger(Object object) {
        try 
        {
            if (object == null) {
               return  BigInteger.ZERO;
            } else {
            	return new BigInteger(object.toString());
            }
        } catch (NullPointerException n) {
            return  BigInteger.ZERO;
        }
    }
	
	public static String isNullStringCheckAndAppendHiphen(Object object) {
        try {
            if (object == null) {
               return "";
            } else {
                 return " - "+(String)object;
            }
        } catch (NullPointerException n) {
            return "";
        }
    }
	public static String isNullStringCheckAndAppendSlash(Object object) {
        try {
            if (object == null) {
               return "";
            } else {
                 return " / "+(String)object;
            }
        } catch (NullPointerException n) {
            return "";
        }
    }
	public static String isNullStringCheckAndReplaceHiphen(Object object) {
        try {
            if (object == null) {
               return "-";
            } else {
                 return (String)object;
            }
        } catch (NullPointerException n) {
            return "";
        }
    }
	public static Character isNullCharCheckAndReplaceHiphen(Object object) {
		try {
			if (object == null) {
				return '-';
			} else {
				return (Character)object.toString().charAt(0);
			}
		} catch (NullPointerException n) {
			return ' ';
		}
	}
	
	public static Character isNullCharacter(Object object) {
        try {
            if (object == null) {
               return '-';
            } else {
            	return (Character)object.toString().charAt(0);
            }
        } catch (NullPointerException n) {
        	return ' ';
        }
    }

    public static boolean isEnglish(String name) {
        // Creating a pattern for whitespaces
        Pattern patt = Pattern.compile("[\\s]");
        // Searching patt in s.
        Matcher mat = patt.matcher(name);
        // Replacing
        name =  mat.replaceAll("");
        return name.matches("[a-zA-Z0-9]+");
    }
    
    public static String removeSpecialCharacters(String str) {
    	 // Remove all characters except letters, digits, commas, and whitespace
        String regex = "[^\\p{L}\\p{N},\\s]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");	
    }
    public static long evaluateExpression(String expression) {
        // Split the expression into individual components
        String[] components = expression.split("\\*");

        // Multiply the components and parse the result to long
        long result = 1;
        for (String component : components) {
            result *= Long.parseLong(component);
        }

        return result;
    }

}
