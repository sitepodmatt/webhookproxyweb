(ns webhookproxyweb.components.filters.routes
  (:require [freeman.ospa.core :refer [dispatch
                                       register-route]]))

(register-route :list-filters "/webhooks/:webhook-id/filters" 
                (fn [{:keys [webhook-id]}] 
                  (dispatch [:change-screen :filters :listing webhook-id])))

(register-route :add-filter "/tasks/:webhook-id/new-filter" 
                (fn [{:keys [webhook-id]}] 
                  (dispatch [:change-screen :filters :update-add webhook-id])))

(register-route :edit-filter "/webhooks/:webhook-id/filters/:filter-id" 
                (fn [{:keys [webhook-id filter-id]}] 
                  (dispatch [:change-screen :filters :update-add webhook-id filter-id])))
