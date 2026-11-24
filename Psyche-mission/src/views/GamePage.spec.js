import { beforeEach, describe, expect, it, vi } from 'vitest'
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
  beforeEach(() => {
    localStorage.clear()
  })

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

  it('saves and unsaves the game from the details page', async () => {
    const wrapper = mountGamePage('jolts-journey')
    await wrapper.vm.$nextTick()

    await wrapper.get('.favorite-btn').trigger('click')
    expect(JSON.parse(localStorage.getItem('favoriteGames'))).toContain('jolts-journey')
    expect(wrapper.get('.favorite-btn').attributes('aria-label')).toBe('Remove from favorites')

    await wrapper.get('.favorite-btn').trigger('click')
    expect(JSON.parse(localStorage.getItem('favoriteGames'))).not.toContain('jolts-journey')
  })

  it('shows a QR prompt for an AR game', async () => {
    const wrapper = mountGamePage('Explore Psyche XR')
    await wrapper.vm.$nextTick()

    expect(wrapper.find('.qr-code-div').exists()).toBe(true)
    expect(wrapper.text()).toContain('Scan the QR code')
  })

  it('shows a download link for a VR game', async () => {
    const wrapper = mountGamePage('psyche-scraper')
    await wrapper.vm.$nextTick()

    expect(wrapper.get('.vr-download-button').text()).toContain('Download the game here')
    expect(wrapper.get('.vr-download-button').attributes('href')).toBeTruthy()
  })
})
