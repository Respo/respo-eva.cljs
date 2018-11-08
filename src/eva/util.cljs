
(ns eva.util )

(defn get-env! [property] (aget (.-env js/process) property))
