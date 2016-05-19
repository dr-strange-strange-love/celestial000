CREATE TABLE public.tle
(
  "NORAD_ID" integer NOT NULL,
  epoch_year integer NOT NULL,
  epoch_day double precision NOT NULL,
  line1 character(70),
  line2 character(70),
  CONSTRAINT pk_tle PRIMARY KEY ("NORAD_ID", epoch_year, epoch_day)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tle
  OWNER TO postgres;