CREATE TABLE public.ssr
(
  "NORAD_ID" integer NOT NULL,
  name character(25),
  obj_type character(40),
  orb_type character(40),
  country character(40),
  CONSTRAINT pk_norad_id PRIMARY KEY ("NORAD_ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.ssr
  OWNER TO postgres;