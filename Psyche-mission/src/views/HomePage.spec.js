import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import HomePage from './HomePage.vue'

function mockMatchMedia() {
  window.matchMedia = (query) => ({
    matches: false,
    media: query,
    addEventListener() {},
    removeEventListener() {},
    addListener() {},
    removeListener() {},
    dispatchEvent() {
      return false
    }
  })
}

describe('HomePage empty search', () => {
  let wrapper

  beforeEach(() => {
    localStorage.clear()
    mockMatchMedia()
  })

  afterEach(() => {
    if (wrapper) {
      wrapper.unmount()
    }
  })

  it('shows an empty message for a nonsense search and restores games when filters are cleared', async () => {
    wrapper = mount(HomePage, {
      global: {
        stubs: {
          FeaturedGame: true,
          GameLink: true,
          QuizModal: true,
          'router-link': true
        }
      }
    })

    expect(wrapper.find('.empty-results').exists()).toBe(false)
    expect(wrapper.find('.game-shelves').exists()).toBe(true)

    wrapper.vm.searchRequest = 'zzzzz-not-a-real-game'
    await wrapper.vm.$nextTick()

    expect(wrapper.get('.empty-results-message').text()).toBe('No games match your search')
    expect(wrapper.find('.game-shelves').exists()).toBe(false)

    await wrapper.get('.empty-results button').trigger('click')

    expect(wrapper.vm.searchRequest).toBe('')
    expect(wrapper.find('.empty-results').exists()).toBe(false)
    expect(wrapper.find('.game-shelves').exists()).toBe(true)
  })

  it('fills quiz results after the quiz is completed', async () => {
    wrapper = mount(HomePage, {
      global: {
        stubs: {
          FeaturedGame: true,
          GameLink: true,
          QuizModal: true,
          'router-link': true
        }
      }
    })

    wrapper.vm.handleQuizResults({
      device: 'Laptop/Desktop',
      difficulty: 'Easy',
      fun: 'Exploring space',
      interest: 'Psyche asteroid'
    })
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.showQuizResults).toBe(true)
    expect(wrapper.vm.quizResults.length).toBeGreaterThan(0)
    expect(wrapper.vm.quizResults.every((game) => game.title)).toBe(true)
  })
})

