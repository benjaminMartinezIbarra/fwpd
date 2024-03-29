(ns fwpd.core
  (:gen-class))

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversations
  {:name          identity
   :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversations vamp-key) value))

(defn parse
  "convert a csv into rows of columns"
  [string]
  (map #(clojure.string/split % #", ")
       (clojure.string/split string #"\n")))

(defn mapify
  "return a seq of maps like {:name \"Edward Cullent\" :glitter-index 10}"
  [rows]
  (map
    (fn [unmapped-row]
      (reduce
        (fn [row-map [vamp-key value]]
          (assoc row-map vamp-key (convert vamp-key value)))
        {}
        (map vector vamp-keys unmapped-row)))
    rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(def gf3 (glitter-filter 3 (mapify (parse (slurp filename)))))