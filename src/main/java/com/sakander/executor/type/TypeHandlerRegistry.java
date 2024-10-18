package com.sakander.executor.type;


import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerRegistry {

    private final Map<JdbcType,TypeHandler<?>> jdbcTypeHandlerMap = new EnumMap<>(JdbcType.class);
    private final Map<Type,Map<JdbcType,TypeHandler<?>>> typeHandlerMap = new ConcurrentHashMap<>();
    private final Map<Class<?>,TypeHandler<?>> allTypeHandlersMap = new HashMap<>();

    private static final Map<JdbcType,TypeHandler<?>> NULL_TYPE_HANDLER_MAP = Collections.emptyMap();

    private Class<? extends TypeHandler> defaultEnumTypeHandler = EnumTypeHandler.class;


    public TypeHandlerRegistry(){
        register(Boolean.class, new BooleanTypeHandler());
        register(boolean.class, new BooleanTypeHandler());
        register(JdbcType.BOOLEAN, new BooleanTypeHandler());
        register(JdbcType.BIT, new BooleanTypeHandler());

        register(Byte.class, new ByteTypeHandler());
        register(byte.class, new ByteTypeHandler());
        register(JdbcType.TINYINT, new ByteTypeHandler());

        register(Short.class, new ShortTypeHandler());
        register(short.class, new ShortTypeHandler());
        register(JdbcType.SMALLINT, new ShortTypeHandler());

        register(Integer.class, new IntegerTypeHandler());
        register(int.class, new IntegerTypeHandler());
        register(JdbcType.INTEGER, new IntegerTypeHandler());

        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());

        register(Float.class, new FloatTypeHandler());
        register(float.class, new FloatTypeHandler());
        register(JdbcType.FLOAT, new FloatTypeHandler());

        register(Double.class, new DoubleTypeHandler());
        register(double.class, new DoubleTypeHandler());
        register(JdbcType.DOUBLE, new DoubleTypeHandler());

        register(Reader.class, new ClobReaderTypeHandler());
        register(String.class, new StringTypeHandler());
        register(String.class, JdbcType.CHAR, new StringTypeHandler());
        register(String.class, JdbcType.CLOB, new ClobTypeHandler());
        register(String.class, JdbcType.VARCHAR, new StringTypeHandler());
        register(String.class, JdbcType.LONGVARCHAR, new StringTypeHandler());
        register(String.class, JdbcType.NVARCHAR, new NStringTypeHandler());
        register(String.class, JdbcType.NCHAR, new NStringTypeHandler());
        register(String.class, JdbcType.NCLOB, new NClobTypeHandler());
        register(JdbcType.CHAR, new StringTypeHandler());
        register(JdbcType.VARCHAR, new StringTypeHandler());
        register(JdbcType.CLOB, new ClobTypeHandler());
        register(JdbcType.LONGVARCHAR, new StringTypeHandler());
        register(JdbcType.NVARCHAR, new NStringTypeHandler());
        register(JdbcType.NCHAR, new NStringTypeHandler());
        register(JdbcType.NCLOB, new NClobTypeHandler());

        register(Object.class, JdbcType.ARRAY, new ArrayTypeHandler());
        register(JdbcType.ARRAY, new ArrayTypeHandler());

        register(BigInteger.class, new BigIntegerTypeHandler());
        register(JdbcType.BIGINT, new LongTypeHandler());

        register(BigDecimal.class, new BigDecimalTypeHandler());
        register(JdbcType.REAL, new BigDecimalTypeHandler());
        register(JdbcType.DECIMAL, new BigDecimalTypeHandler());
        register(JdbcType.NUMERIC, new BigDecimalTypeHandler());

        register(InputStream.class, new BlobInputStreamTypeHandler());
        register(Byte[].class, new ByteObjectArrayTypeHandler());
        register(Byte[].class, JdbcType.BLOB, new BlobByteObjectArrayTypeHandler());
        register(Byte[].class, JdbcType.LONGVARBINARY, new BlobByteObjectArrayTypeHandler());
        register(byte[].class, new ByteArrayTypeHandler());
        register(byte[].class, JdbcType.BLOB, new BlobTypeHandler());
        register(byte[].class, JdbcType.LONGVARBINARY, new BlobTypeHandler());
        register(JdbcType.LONGVARBINARY, new BlobTypeHandler());
        register(JdbcType.BLOB, new BlobTypeHandler());

        register(Date.class, new DateTypeHandler());
        register(Date.class, JdbcType.DATE, new DateOnlyTypeHandler());
        register(Date.class, JdbcType.TIME, new TimeOnlyTypeHandler());
        register(JdbcType.TIMESTAMP, new DateTypeHandler());
        register(JdbcType.DATE, new DateOnlyTypeHandler());
        register(JdbcType.TIME, new TimeOnlyTypeHandler());

        register(java.sql.Date.class, new SqlDateTypeHandler());
        register(java.sql.Time.class, new SqlTimeTypeHandler());
        register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());

        register(Instant.class, new InstantTypeHandler());
        register(LocalDateTime.class, new LocalDateTimeTypeHandler());
        register(LocalDate.class, new LocalDateTypeHandler());
        register(LocalTime.class, new LocalTimeTypeHandler());
        register(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        register(OffsetTime.class, new OffsetTimeTypeHandler());
        register(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        register(Month.class, new MonthTypeHandler());
        register(Year.class, new YearTypeHandler());
        register(YearMonth.class, new YearMonthTypeHandler());
        // issue #273
        register(Character.class, new CharacterTypeHandler());
        register(char.class, new CharacterTypeHandler());
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return getTypeHandler((Type) type, null);
    }

    @SuppressWarnings("unchecked")
    private <T> TypeHandler<T> getTypeHandler(Type type, JdbcType jdbcType){
        Map<JdbcType,TypeHandler<?>> jdbcHandlerMap = getJdbcHandlerMap(type);
        TypeHandler<?> handler = null;
        if(jdbcHandlerMap != null){
            handler = jdbcHandlerMap.get(jdbcType);
            if(handler == null){
                handler = jdbcHandlerMap.get(null);
            }
            if(handler == null){
                handler = pickSoleHandler(jdbcHandlerMap);
            }
        }
        return (TypeHandler<T>) handler;
    }

    private Map<JdbcType,TypeHandler<?>> getJdbcHandlerMap(Type type){
        Map<JdbcType,TypeHandler<?>> jdbcHandlerMap = typeHandlerMap.get(type);
        if(jdbcHandlerMap != null){
            return NULL_TYPE_HANDLER_MAP.equals(jdbcHandlerMap) ? null : jdbcHandlerMap;
        }

        if(type instanceof Class){
            Class<?> clazz = (Class<?>) type;
            if(Enum.class.isAssignableFrom(clazz)){
                if(clazz.isAnonymousClass()){
                    return getJdbcHandlerMap(clazz.getSuperclass());
                }
                jdbcHandlerMap = getJdbcHandlerMapForEnumInterfaces(clazz,clazz);
                if(jdbcHandlerMap == null){
                    register(clazz,getInstance(clazz,defaultEnumTypeHandler));
                    return typeHandlerMap.get(clazz);
                }
            }else{
                jdbcHandlerMap = getJdbcHandlerMapForSuperclass(clazz);
            }
        }
        typeHandlerMap.put(type,jdbcHandlerMap == null ? NULL_TYPE_HANDLER_MAP : jdbcHandlerMap);
        return jdbcHandlerMap;
    }

    private Map<JdbcType,TypeHandler<?>> getJdbcHandlerMapForEnumInterfaces(Class<?> clazz,Class<?> enumClazz){
        for(Class<?> iface :  clazz.getInterfaces()){
            Map<JdbcType,TypeHandler<?>> jdbcHandlerMap = typeHandlerMap.get(iface);
            if(jdbcHandlerMap == null){
                jdbcHandlerMap = getJdbcHandlerMapForEnumInterfaces(iface,enumClazz);
            }
            if(jdbcHandlerMap != null){
                HashMap<JdbcType,TypeHandler<?>> newMap = new HashMap<>();
                for(Map.Entry<JdbcType,TypeHandler<?>> entry : jdbcHandlerMap.entrySet()){
                    newMap.put(entry.getKey(),getInstance(enumClazz,entry.getValue().getClass()));
                }
                return newMap;
            }
        }
        return null;
    }

    private Map<JdbcType,TypeHandler<?>> getJdbcHandlerMapForSuperclass(Class<?> clazz){
        Class<?> superclass = clazz.getSuperclass();
        if(superclass == null || Object.class.equals(superclass)){
            return null;
        }
        Map<JdbcType,TypeHandler<?>> jdbcHandlerMap = typeHandlerMap.get(superclass);
        if(jdbcHandlerMap != null){
            return jdbcHandlerMap;
        }
        return getJdbcHandlerMapForSuperclass(superclass);
    }

    private TypeHandler<?> pickSoleHandler(Map<JdbcType,TypeHandler<?>> jdbcHandlerMap){
        TypeHandler<?> soleHandler = null;
        for(TypeHandler<?> handler : jdbcHandlerMap.values()){
            if(soleHandler == null){
                soleHandler = handler;
            }else if(!handler.getClass().equals(soleHandler.getClass())){
                // More than one type handlers registerd
                return null;
            }
        }
        return soleHandler;
    }

    @SuppressWarnings("unchecked")
    public <T> TypeHandler<T> getInstance(Class<?> javaTypeClass,Class<?> typeHandlerClass){
        if(javaTypeClass != null){
            try {
                Constructor<?> c = typeHandlerClass.getConstructor(Class.class);
                return (TypeHandler<T>) c.newInstance(javaTypeClass);
            }catch (NoSuchMethodException ignored){
                // ignore
            }catch (Exception e){
                throw new TypeException("Failed invoking constructor for handler" + typeHandlerClass,e);
            }
        }
        try {
            Constructor<?> c = typeHandlerClass.getConstructor();
            return (TypeHandler<T>) c.newInstance();
        }catch (Exception e){
            throw new TypeException("Unable to find a usable constructor for" + typeHandlerClass,e);
        }
    }

    public void register(JdbcType jdbcType,TypeHandler<?> handler){
        jdbcTypeHandlerMap.put(jdbcType,handler);
    }

    // java type + handler
    public <T> void register(Class<T> javaType, TypeHandler<? extends T> typeHandler) {
        register((Type) javaType, typeHandler);
    }

    private <T> void register(Type javaType,TypeHandler<? extends T> typeHandler){
        MappedJdbcTypes mappedJdbcTypes = typeHandler.getClass().getAnnotation(MappedJdbcTypes.class);
        if(mappedJdbcTypes != null){
            for(JdbcType handledJdbcType : mappedJdbcTypes.value()){
                register(javaType,handledJdbcType,typeHandler);
            }
            if(mappedJdbcTypes.includeNullJdbcType()){
                register(javaType,null,typeHandler);
            }
        }else{
            register(javaType,null,typeHandler);
        }
    }

    private void register(Type javaType,JdbcType jdbcType,TypeHandler<?> handler){
        if(javaType != null){
            Map<JdbcType,TypeHandler<?>> map = typeHandlerMap.get(javaType);
            if(map == null || map == NULL_TYPE_HANDLER_MAP){
                map = new HashMap<>();
            }
            map.put(jdbcType,handler);
            typeHandlerMap.put(javaType,map);
        }
        allTypeHandlersMap.put(handler.getClass(),handler);
    }
}
