import { beforeEach, describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import GameRating from './GameRating.vue'

describe('GameRating', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  it('saves a star rating to localStorage', async () => {
    const wrapper = mount(GameRating, {
      props: { gameId: 'jolts-journey' }
    })

    await wrapper.findAll('.star-button').at(3).trigger('click')

    expect(wrapper.vm.currentRating).toBe(4)
    expect(wrapper.get('.rating-text').text()).toContain('4 out of 5')
    expect(localStorage.getItem('gameRating-jolts-journey')).toBe('4')
  })

  it('clears a saved rating', async () => {
    const wrapper = mount(GameRating, {
      props: { gameId: 'jolts-journey' }
    })

    await wrapper.findAll('.star-button').at(4).trigger('click')
    expect(wrapper.get('.rating-text').text()).toContain('5 out of 5')

    await wrapper.get('.clear-button').trigger('click')

    expect(wrapper.vm.currentRating).toBe(0)
    expect(localStorage.getItem('gameRating-jolts-journey')).toBeNull()
  })

  it('restores a previously saved rating', async () => {
    localStorage.setItem('gameRating-jolts-journey', '3')

    const wrapper = mount(GameRating, {
      props: { gameId: 'jolts-journey' }
    })
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.currentRating).toBe(3)
    expect(wrapper.get('.rating-text').text()).toContain('3 out of 5')
  })
})
