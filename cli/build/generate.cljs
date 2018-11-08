

(ns build.generate
  (:require ["fs" :as fs]
            [clojure.string :as string]))
(def ns-code "
(ns eva.icons
  (:require [respo.core :refer [create-element defcomp i span style]]))

(def s {:display :line-block, :width \"100%\", :height \"100%\"})

(def icon-base {:display :inline-block, :width 20, :height 20})

(defcomp wrap [icon css]
  (span {:style (merge icon-base css)}
    (style {:scoped true :innerHTML (str \"svg { stroke: \" (or (:color css) \"black\") \";}\")})
    (icon)))

")

(defn makeup-comp [x] "(defcomp eva-$name [] (i {:style s :innerHTML $html}))")

(let [files (js->clj (fs/readdirSync "eva"))
      icon-names (map (fn [x] (string/replace x ".svg" "")) files)
      code-blocks (map (fn [x] (-> (makeup-comp x)
                                   (string/replace "$name" x)
                                   (string/replace "$html"
                                      (pr-str (fs/readFileSync (str "eva/" x ".svg") "utf8"))))) icon-names)]
  (fs/writeFileSync "icons/eva/icons.cljs" (str ns-code (string/join "\n\n" code-blocks))))
