# Sakander
1. com/sakander/config/DatabaseConfig 应该是读的系统的连接池，不要自己控制连接
2. printStackTrace 都是禁止的，你吃掉了excetion
3. JdbcUtils.java:36 error不能这样处理，你相当于很详细的error变成了不清楚的error, 业务无法知晓什么错误， 所有的 throw new RuntimeException 都是禁止的