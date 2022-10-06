import { createStore } from 'vuex'
import ModuleUser from './user'
import ModuleProblem from './problem' // TODO: THIS IS A TEST FUNCTION, MAY GET REMOVED LATER
import ModuleProblemList from './problemList'
export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    problem: ModuleProblem,
    problemList: ModuleProblemList
  }
})
