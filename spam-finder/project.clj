(defproject spam-finder "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring/ring-defaults "0.1.5"]
                 [ring-server "0.3.1"]
                 [incanter/incanter-core "1.5.6"]
                 [lib-noir "0.7.6"]
                 [selmer "0.8.2"]
                 [markdown-clj "0.9.67"]
                 [com.novemberain/monger "2.0.0"]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler spam-finder.handler/app
         :init spam-finder.handler/init
         :destroy spam-finder.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}}
  :jvm-opts ["-Xmx1G"]
  :main spam-finder.repl)
