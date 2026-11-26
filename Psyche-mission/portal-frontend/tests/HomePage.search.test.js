import { describe, it, expect, beforeEach, vi } from "vitest";
import { mount, flushPromises } from "@vue/test-utils";
import SearchBar from "../src/components/SearchBar.vue";
import HomePage from "../src/views/HomePage.vue";
import gamesData from "./fixtures/games.json"

vi.mock("@/utils/favoriteService", () => ({
    loadFavoritesFromServer: vi.fn()
}));

const games = gamesData.games;

describe("HomePage Search Accuracy", () => {
    afterEach(() => {
        vi.unstubAllGlobals();
    });

    it.each([
        {
            search: "Psyche Scraper",
            expected: ["Psyche Scraper"]
        },
        {
            search: "Astro Academy",
            expected: ["ASTRO ACADEMY"]
        },
        {
            search: "Scraper",
            expected: ["Psyche Scraper"]
        },
        {
            search: "Quest",
            expected: ["Psyche Surface Quest", "Psyche Quest", "JUMP TO PSYCHE", "PSYCHE RUN", "Astroquest: Psyche's Odyssey"]
        },
        {
            search: "Expedition",
            expected: ["Psyche 16: Expedition VR", "INTERWORLDS", "EXPEDITION PSYCHE"]
        },
        {
            search: "fakegame777",
            expected: []
        },
        {
            search: "88depparw",
            expected: []
        },
        {
            search: "exploring outer space",
            expected: ["MISSION PSYCHE"]
        },
        {
            search: "real NASA data",
            expected: ["NASA Psyche Mission: Surface Data VR"]
        },
        {
            search: "asteroid's",
            expected: ["PsycheVision", "PSYCHE RECON SIM", "TRIVIA TREK"]
        },
        {
            search: "PSYCHE SCANNER",
            expected: ["PSYCHE SCANNER"]
        },
        {
            search: "miSt",
            expected: ["MIST"]
        },
        {
            search: "HoT",
            expected: ["SLING SHOT PSYCHE", "JOURNEY TO PSYCHE 2"]
        },
        {
            search: "asTEROid MIssION",
            expected: ["Welcome To Psyche", "Asteroid Mission"]
        },
        {
            search: "   Rhythms of Psyche",
            expected: ["Rhythms of Psyche"]
        },
        {
            search: "Super Psyched    ",
            expected: ["Super Psyched"]
        },
        {
            search: "Psyche    Salvage",
            expected: ["Psyche Salvage"]
        },
        {
            search: "",
            expected: games.map(game => game.title)
        },
        {
            search: "     ",
            expected: games.map(game => game.title)
        },
        {
            search: ":",
            expected: ["ASTEROID AND ASPIRATIONS", "Around Psyche in 448 Days", "Psyche 16: Expedition VR", "NASA Psyche Mission: Surface Data VR", "Understand Psyche: Psyche Comparison Simulator", "Astroquest: Psyche's Odyssey", "EXPEDITION PSYCHE", "INTERWORLDS", "JOLTS JOURNEY", "On Console: Psyche", "Core Informant: Psyche", "Psyche Exploration: Build and Launch", "Psyche Investigator", "Psyche Salvage", "ZEPHYR EXPRESS"]
        },
        ])("returns the correct games for search", async ({search, expected}) => {
        //Mock the backend response with the game data
        vi.stubGlobal("fetch", vi.fn().mockResolvedValue({
            ok: true,
            json: async () => games
        }));

        vi.stubGlobal("matchMedia", vi.fn().mockImplementation(() => ({
            matches: false,
            addEventListener: vi.fn(),
            removeEventListener: vi.fn(),
        })))

        const wrapper = mount(HomePage);

        await flushPromises();

        wrapper.vm.searchRequest = search;

        const result = wrapper.vm.filteredGames.map(game => game.title);

        expect(result.sort()).toEqual(expected.sort());

        wrapper.unmount();
    });
});