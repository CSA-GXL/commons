
SELECT *, ROUND(6378.137 * 2 * ASIN(SQRT(
    POW(SIN(( @{arg0} * PI() / 180 - lat * PI() / 180) / 2),2)
    + COS( @{arg0} * PI() / 180) * COS(lat * PI() / 180)
    * POW(SIN(( @{arg1} * PI() / 180 - lon * PI() / 180) / 2), 2)
)) * 1000) AS distance
FROM demo_address
ORDER BY distance ASC
limit 0, 1