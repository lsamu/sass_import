import { ref } from "vue"


export const useDemo = (init = 10) => {

    const count = ref(init)

    // 固定加几？increaseCount参数
    const increase = (increaseCount?: number): void => {
      if (typeof increaseCount !== 'undefined') {
        count.value += increaseCount
      } else {
        count.value += 1
      }
    }

    const decrease = (decreaseCount?: number): void => {
      if (typeof decreaseCount !== 'undefined') {
        count.value -= decreaseCount
      } else {
        count.value -= 1
      }
    }
    return {
      count,
      increase,
      decrease
    }
  }