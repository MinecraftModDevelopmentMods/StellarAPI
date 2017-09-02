package stellarapi.api.lib.config;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public interface IPropertyType {

	/** Checks if certain string is valid. */
	public boolean isValid(String strValue);
	/** Gets the most relevant valid string with the parameter. */
	public String toValidString(String incomplete);
	/** Gets value as string. */
	public String toString(Object value);
	/** Sets value with string. if it fails, gives default value or throws appropriate exception. */
	public Object fromString(String value);


	/** Gives possible ids for alternatives. the first element is the default one.
	 * null for native types.
	 * empty string for those without any variants.
	 * */
	public String[] alternativeIds();

	/**
	 * Gives alternative expansion defined on native types. It's immutable.
	 * null for native types.
	 * */
	public ITypeExpansion<?> alternative(String altId);

	static abstract class NativeType implements IPropertyType {
		@Override
		public String toString(Object value) {
			return String.valueOf(value);
		}
		@Override
		public String[] alternativeIds() {
			return null;
		}
		@Override
		public ITypeExpansion<?> alternative(String altId) {
			return null;
		}

		protected String extractNumber(String str) {
			str = str.replaceAll("[^\\d+-]+", "");
			if(str.startsWith("-"))
				str.replaceFirst("-", "M");
			else if(str.startsWith("+"))
				str.replace("+", "P");
			str = str.replaceAll("(\\+|-)+", "");
			str = str.replaceFirst("P", "+").replaceFirst("M", "-");
			return str;
		}

		protected String extractDecimal(String str) {
			str = str.replaceAll("[^\\d\\.+-]+", "");
			if(str.startsWith("-"))
				str.replaceFirst("-", "M");
			else if(str.startsWith("+"))
				str.replace("+", "P");
			str = str.replaceAll("(\\+|-)+", "");
			str = str.replaceFirst("P", "+").replaceFirst("M", "-");
			return str; // TODO This need to be fleshed out. Maybe non-appropriate
		}
	}

	// array type
	static class ArrayType implements IPropertyType {
		private IPropertyType elementPropType;
		private Class<?> elementClass;

		ArrayType(IPropertyType type, Class<?> compClass) {
			this.elementPropType = type;
			this.elementClass = compClass;
		}

		@Override
		public boolean isValid(String strValue) {
			if(!strValue.startsWith("[") || !strValue.endsWith("]"))
				return false;
			strValue = strValue.substring(1, strValue.length()-1);
			for(String individual : strValue.split(", ")) {
				if(!elementPropType.isValid(individual))
					return false;
			}

			return true;
		}

		@Override
		public String toValidString(String incomplete) {
			if(incomplete.startsWith("["))
				incomplete = incomplete.substring(1);
			if(!incomplete.endsWith("]"))
				incomplete = incomplete.substring(0, incomplete.length() - 1);

			String[] els = incomplete.split(",");
			for(int i = 0; i < els.length; i++) {
				if(i > 0 && els[i].startsWith(" "))
					els[i] = els[i].substring(1);
				els[i] = elementPropType.toValidString(els[i]);
			}

			return Arrays.toString(els);
		}

		@Override
		public String toString(Object value) {
			int numEls = Array.getLength(value);
			String[] strArray = new String[numEls];
			for(int i = 0; i < numEls; i++)
				strArray[i] = elementPropType.toString(Array.get(value, i));
			return Arrays.toString(strArray);
		}

		@Override
		public Object fromString(String value) {
			value = value.substring(1, value.length()-1);
			String[] subValues = value.split(", ");
			Object array = Array.newInstance(this.elementClass, subValues.length);
			for(int i = 0; i < subValues.length; i++)
				Array.set(array, i, elementPropType.fromString(subValues[i]));
			return array;
		}

		@Override
		public String[] alternativeIds() {
			return new String[] {""};
		}

		@Override
		public ITypeExpansion<?> alternative(String altId) {
			return ITypeExpansion.LIST;
		}
	}

	static class FixedListType implements IPropertyType {
		private Class<?> elementType;
		private IPropertyType elementPropType;

		FixedListType(Class<?> elementType, IPropertyType elementPropType) {
			this.elementType = elementType;
			this.elementPropType = elementPropType;
		}

		@Override
		public boolean isValid(String strValue) {
			if(!strValue.startsWith("[") || !strValue.endsWith("]"))
				return false;
			strValue = strValue.substring(1, strValue.length()-1);
			for(String individual : strValue.split(", ")) {
				if(!elementPropType.isValid(individual))
					return false;
			}

			return true;
		}

		@Override
		public String toValidString(String incomplete) {
			if(incomplete.startsWith("["))
				incomplete = incomplete.substring(1);
			if(!incomplete.endsWith("]"))
				incomplete = incomplete.substring(0, incomplete.length() - 1);

			String[] els = incomplete.split(",");
			for(int i = 0; i < els.length; i++) {
				if(i > 0 && els[i].startsWith(" "))
					els[i] = els[i].substring(1);
				els[i] = elementPropType.toValidString(els[i]);
			}

			return Arrays.toString(els);
		}

		@Override
		public String toString(Object value) {
			List listValue = (List) value;
			int numEls = listValue.size();
			String[] strArray = new String[numEls];
			for(int i = 0; i < numEls; i++)
				strArray[i] = elementPropType.toString(listValue.get(i));
			return Arrays.toString(strArray);
		}

		@Override
		public Object fromString(String value) {
			value = value.substring(1, value.length()-1);
			String[] subValues = value.split(", ");
			List array = Lists.newArrayList(subValues.length);
			for(int i = 0; i < subValues.length; i++)
				array.set(i, elementPropType.fromString(subValues[i]));
			return array;
		}

		@Override
		public String[] alternativeIds() {
			return new String[] {""};
		}

		@Override
		public ITypeExpansion<?> alternative(String altId) {
			return ITypeExpansion.ARRAY;
		}
	}

	static final BooleanType boolT = new BooleanType();
	static final ArrayType boolAT = new ArrayType(boolT, boolean.class);
	static final ArrayType BoolAT = new ArrayType(boolT, Boolean.class);
	static class BooleanType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			return strValue.equals(((Boolean)false).toString())
					|| strValue.equals(((Boolean)true).toString());
		}
		@Override
		public String toValidString(String incomplete) {
			String fstr = String.valueOf(false);
			String tstr = String.valueOf(true);

			if(incomplete.contains(fstr) || fstr.contains(incomplete))
				return fstr;
			if(incomplete.contains(tstr) || tstr.contains(incomplete))
				return tstr;

			int numT = incomplete.split("T|t").length;
			int numF = incomplete.split("F|f").length;

			if(numT < numF)
				return fstr;
			else if(numT > numF)
				return tstr;

			return null;
		}
		@Override
		public Object fromString(String value) {
			return Boolean.getBoolean(value);
		}
	}

	// TODO what does byte mean by default - number or character?
	// I guess it's character, but just need to put a bit more thought on this.
	static final ByteType byteT = new ByteType();
	static final ArrayType byteAT = new ArrayType(byteT, byte.class);
	static final ArrayType ByteAT = new ArrayType(byteT, Byte.class);
	static class ByteType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			try {
				Byte.parseByte(strValue);
				return true;
			} catch(NumberFormatException exception) {
				return false;
			}
		}
		@Override
		public String toValidString(String incomplete) {
			return null;
		}
		@Override
		public Object fromString(String value) {
			return Byte.parseByte(value);
		}
	}
	static final CharType charT = new CharType();
	static final ArrayType charAT = new ArrayType(charT, char.class);
	static final ArrayType CharacterAT = new ArrayType(charT, Character.class);
	static class CharType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			return strValue.length() == 1;
		}
		@Override
		public String toValidString(String incomplete) {
			return incomplete.isEmpty()? ((Character)' ').toString() : incomplete.substring(0, 1);
		}
		@Override
		public Object fromString(String value) {
			return value.charAt(0);
		}
	}
	static final ShortType shortT = new ShortType();
	static final ArrayType shortAT = new ArrayType(shortT, short.class);
	static final ArrayType ShortAT = new ArrayType(shortT, Short.class);
	static class ShortType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			try {
				Short.parseShort(strValue);
				return true;
			} catch(NumberFormatException exception) {
				return false;
			}
		}
		@Override
		public String toValidString(String incomplete) {
			incomplete = this.extractNumber(incomplete);
			while(incomplete.length() > 0) {
				try {
					Short.parseShort(incomplete);
					return incomplete;
				} catch(NumberFormatException exception) {}
				incomplete = incomplete.substring(0, incomplete.length()-1);
			}

			return null; // Can't reach here
		}
		@Override
		public Object fromString(String value) {
			return Short.parseShort(value);
		}
	}
	static final IntType intT = new IntType();
	static final ArrayType intAT = new ArrayType(intT, int.class);
	static final ArrayType IntegerAT = new ArrayType(intT, Integer.class);
	static class IntType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			try {
				Integer.parseInt(strValue);
				return true;
			} catch(NumberFormatException exception) {
				return false;
			}
		}
		@Override
		public String toValidString(String incomplete) {
			incomplete = this.extractNumber(incomplete);
			while(incomplete.length() > 0) {
				try {
					Integer.parseInt(incomplete);
					return incomplete;
				} catch(NumberFormatException exception) {}
				incomplete = incomplete.substring(0, incomplete.length()-1);
			}

			return null; // Can't reach here
		}
		@Override
		public Object fromString(String value) {
			return Integer.parseInt(value);
		}
	}
	static final LongType longT = new LongType();
	static final ArrayType longAT = new ArrayType(longT, long.class);
	static final ArrayType LongAT = new ArrayType(longT, Long.class);
	static class LongType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			try {
				Long.parseLong(strValue);
				return true;
			} catch(NumberFormatException exception) {
				return false;
			}
		}
		@Override
		public String toValidString(String incomplete) {
			incomplete = this.extractNumber(incomplete);
			while(incomplete.length() > 0) {
				try {
					Long.parseLong(incomplete);
					return incomplete;
				} catch(NumberFormatException exception) {}
				incomplete = incomplete.substring(0, incomplete.length()-1);
			}

			return null; // Can't reach here
		}
		@Override
		public Object fromString(String value) {
			return Long.parseLong(value);
		}
	}

	static final FloatType floatT = new FloatType();
	static final ArrayType floatAT = new ArrayType(floatT, float.class);
	static final ArrayType FloatAT = new ArrayType(floatT, Float.class);
	static class FloatType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			try {
				Float.parseFloat(strValue);
				return true;
			} catch(NumberFormatException exception) {
				return false;
			}
		}
		@Override
		public String toValidString(String incomplete) {
			return this.extractDecimal(incomplete);
		}
		@Override
		public Object fromString(String value) {
			return Float.parseFloat(value);
		}
	}
	static final DoubleType doubleT = new DoubleType();
	static final ArrayType doubleAT = new ArrayType(doubleT, double.class);
	static final ArrayType DoubleAT = new ArrayType(doubleT, Double.class);
	static class DoubleType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			try {
				Double.parseDouble(strValue);
				return true;
			} catch(NumberFormatException exception) {
				return false;
			}
		}
		@Override
		public String toValidString(String incomplete) {
			return this.extractDecimal(incomplete);
		}
		@Override
		public Object fromString(String value) {
			return Double.parseDouble(value);
		}
	}

	static final StringType stringT = new StringType();
	static final ArrayType stringAT = new ArrayType(stringT, String.class);
	static final class StringType extends NativeType {
		@Override
		public boolean isValid(String strValue) {
			return true;
		}
		@Override
		public String toValidString(String incomplete) {
			return incomplete;
		}
		@Override
		public Object fromString(String value) {
			return value;
		}
		@Override
		public String toString(Object value) {
			return (String)value;
		}
	}
}