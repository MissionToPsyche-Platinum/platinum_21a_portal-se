import { describe, expect, it, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import GamePage from './GamePage.vue'

function mountGamePage(id, replace = vi.fn()) {
  return mount(GamePage, {
    props: { id },
    global: {
      mocks: {
        $router: { replace }
      },
      stubs: {
        GameLink: true,
        GameRating: true,
        'router-link': true
      }
    }
  })
}

describe('GamePage', () => {
  it('shows title and details for a known game id', async () => {
    const wrapper = mountGamePage('jolts-journey')
    await wrapper.vm.$nextTick()

    expect(wrapper.get('h1').text()).toBe('JOLTS JOURNEY')
    expect(wrapper.get('.hero-meta').text()).toContain('Hard')
    expect(wrapper.get('.hero-meta').text()).toContain('Adventure')
    expect(wrapper.text()).toContain('Nickel - 2020-2021')
  })

  it('does not include the current game in suggestions', async () => {
    const wrapper = mountGamePage('jolts-journey')
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.suggestedGames.every((game) => game.id !== 'jolts-journey')).toBe(true)
  })

  it('sends unknown game ids to the 404 page', async () => {
    const replace = vi.fn()
    const wrapper = mountGamePage('jolts-journey', replace)
    await wrapper.vm.$nextTick()

    await wrapper.setProps({ id: 'not-a-real-game' })

    expect(replace).toHaveBeenCalledWith({ name: 'NotFound' })
  })
})
