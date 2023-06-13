<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="sepac-scroll-horizontal" :class="rootClasses">
    <!-- CONTROLLO SINISTRO -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-if="controls">
      <div
        v-touch-repeat.mouse="scrollLeft"
        class="col-auto sepac-scroll-horizontal-control sepac-scroll-horizontal-control--left"
        @click="scrollLeft"
      >
        <slot
          name="control-left"
          :is-left-edge="isLeftEdge"
          :is-right-edge="isRightEdge"
        >
          <template v-if="!controlsFloating || !isLeftEdge">
            <q-btn
              round
              flat
              dense
              class="sepac-scroll-horizontal-control__button sepac-scroll-horizontal-control__button-left"
              :disable="isLeftEdge"
            >
              <q-icon
                name="chevron_left"
                size="xl"
                class="sepac-scroll-horizontal-control__icon"
              />
            </q-btn>
          </template>
        </slot>
      </div>
    </template>

    <!-- SCROLLER -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <div
      class="sepac-scroll-horizontal-scroller-wrapper relative-position overflow-hidden"
      :class="wrapperClasses"
    >
      <div
        v-scroll="scrolled"
        v-mutation="mutation"
        v-touch-pan.horizontal.prevent.mouse="handlePan"
        ref="container"
        class="sepac-scroll-horizontal-scroller scroll"
        :class="scrollClasses"
      >
        <slot />

        <q-resize-observer debounce="250" @resize="onContainerResize" />
      </div>
    </div>

    <!-- CONTROLLO DESTRO -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-if="controls">
      <div
        v-touch-repeat.mouse="scrollRight"
        class="col-auto sepac-scroll-horizontal-control sepac-scroll-horizontal-control--right"
        @click="scrollRight"
      >
        <slot
          name="control-right"
          :is-left-edge="isLeftEdge"
          :is-right-edge="isRightEdge"
        >
          <template v-if="!controlsFloating || !isRightEdge">
            <q-btn
              round
              flat
              dense
              class="sepac-scroll-horizontal-control__button sepac-scroll-horizontal-control__button-right"
              :disable="isRightEdge"
            >
              <q-icon
                name="chevron_right"
                size="xl"
                class="sepac-scroll-horizontal-control__icon"
              />
            </q-btn>
          </template>
        </slot>
      </div>
    </template>
  </div>
</template>

<script>
import { scroll, dom } from "quasar";

const { width } = dom;

const {
  getHorizontalScrollPosition,
  setHorizontalScrollPosition,
  getScrollWidth
} = scroll;

const defaultScrollDelta = container => {
  if (!container || !container.children || !container.children[0]) return 100;
  return container.children[0].clientWidth;
};

export default {
  name: "sepacScrollHorizontal",
  props: {
    classes: { type: String, required: false, default: "" },
    scrollDelta: {
      type: Function,
      required: false,
      default: defaultScrollDelta
    },
    controls: { type: Boolean, required: false, default: false },
    controlsFloating: { type: Boolean, required: false, default: false },
    scrollDuration: { type: Number, required: false, default: 300 },
    scrollbar: { type: Boolean, required: false, default: false },
    draggable: { type: Boolean, required: false, default: false },
    edgeThreshold: { type: Number, required: false, default: 5 },
    controlsInvisibleOnThreshold: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      x: 0,
      y: 0,
      containerWidth: 0,
      containerScrollWidth: 0
    };
  },
  computed: {
    rootClasses() {
      let result = [];

      if (this.controls) {
        result.push("sepac-scroll-horizontal--controls row items-center");
      }

      if (this.controls && !this.controlsFloating) {
        result.push("sepac-scroll-horizontal--controls-fixed");
      }

      if (this.controls && this.controlsFloating) {
        result.push("sepac-scroll-horizontal--controls-floating");
      }

      if (this.draggable) result.push("sepac-scroll-horizontal--draggable");

      if (this.scrollbar) {
        result.push("sepac-scroll-horizontal--scrollbar");
      }

      if (this.controlsInvisibleOnThreshold) {
        result.push("sepac-scroll-horizontal--controls-invisible");
      }

      if (this.isLeftEdge) {
        result.push("sepac-scroll-horizontal--left-edge");
      }

      if (this.isRightEdge) {
        result.push("sepac-scroll-horizontal--right-edge");
      }

      return result;
    },
    wrapperClasses() {
      let result = [];
      if (this.controls && !this.controlsFloating) result.push("col");
      return result;
    },
    scrollClasses() {
      let result = [];
      return [...result, this.classes];
    },
    isLeftEdge() {
      return this.x <= this.edgeThreshold;
    },
    isRightEdge() {
      let max =
        this.containerScrollWidth - this.containerWidth - this.edgeThreshold;
      return this.x >= max;
    }
  },
  mounted() {
    this.updateContainerInfo();
  },
  methods: {
    handlePan(details) {
      if (!this.draggable) return;
      let el = this.$refs.container;
      let x = getHorizontalScrollPosition(el);

      // isFinal significa che si tratta della fine della gestione del pan
      // => Ã¨ il momento giusto per fare eventuali aggiustamenti
      if (details.isFinal) {
        // VelocitÃ  px/ms
        let v = details.distance.x / details.duration;

        // Creiamo un delta proporzionale alla velocitÃ  di drag
        //
        // Se la velocitÃ  Ã¨ bassa (< 1.5) non aggiungiamo nessun delta
        // In questo modo l'utente che ha scrollato parecchio e velocemente scorrerÃ  piÃ¹ elementi
        // rispetto all'utente che ha scrollato poco anche se forte
        let delta = 0;
        if (v >= 2) delta = details.offset.x * v;

        x = x - delta;
        x = Math.max(0, x);
        x = Math.min(getScrollWidth(el), x);

        setHorizontalScrollPosition(el, x, this.scrollDuration);
        // console.log(details);
        // console.log({ v, delta });

        // // Aggiungiamo un'accelerazione al movimento
        // // basiamo l'accelerazione sulla forza usata dall'utente per scrollare
        // // cioÃ¨ il rapporto tra distanza percorsa e durata dello scroll.
        // //
        // // Se l'utente percorre poca distanza in tanto tempo vuol dire che sta scrollando piano
        // // Se l'utente percorre molta distanza in poco tempo vuol dire che ha dato uno strappo allo scroll
        // // perchÃ© si vuole muovere velocemente
        // let a = Math.abs(details.offset.x) / Math.abs(details.duration);
        // a = Math.min(a, 7);
        // a = Math.max(a, 0);
        //
        // // Creiamo un delta a partire dall'accelerazione calcolata
        // // Se l'accelerazione Ã¨ bassa (< 1.5) non aggiungiamo nessun delta
        // //
        // // Se invece l'accelerazione Ã¨ elevata
        // // => il delta che aggiungiamo si basa sull'accelerazione e sulla distanza percorsa
        // //
        // // In questo modo l'utente che ha scrollato parecchio e velocemente scorrerÃ  piÃ¹ elementi
        // // rispetto all'utente che ha scrollato poco anche se forte
        // let delta = a < 1.5 ? 0 : details.offset.x * a;
        // x = x - delta;
        // x = Math.max(0, x);
        // x = Math.min(getScrollWidth(el), x);
        //
        // let time = 300;
        // setHorizontalScrollPosition(el, x, time);
        // // console.log({ delta, a, time });
      } else {
        x = x - details.delta.x;
        x = Math.max(0, x);
        x = Math.min(getScrollWidth(el), x);
        setHorizontalScrollPosition(el, x);
      }
    },
    mutation() {
      this.updateContainerInfo();
    },
    onContainerResize() {
      this.updateContainerInfo();
    },
    updateContainerInfo() {
      this.containerWidth = width(this.$refs.container);
      this.containerScrollWidth = getScrollWidth(this.$refs.container);
    },
    scrolled(y, x) {
      this.y = y;
      this.x = x;
    },
    scrollLeft() {
      let el = this.$refs.container;
      let delta = -this.scrollDelta(el);

      let x = getHorizontalScrollPosition(el);
      x = Math.max(0, x + delta);
      setHorizontalScrollPosition(el, x, this.scrollDuration);
    },
    scrollRight() {
      let el = this.$refs.container;
      let delta = this.scrollDelta(el);

      let x = getHorizontalScrollPosition(el);
      x = Math.min(getScrollWidth(el), x + delta);
      setHorizontalScrollPosition(el, x, this.scrollDuration);
    }
  }
};
</script>

<style lang="sass">
// STILI SCROLL ORIZZONTALE
// ------------------------
.sepac-scroll-horizontal--controls-floating
  position: relative

// STILI CONTROLLI
// ------------------
.sepac-scroll-horizontal--controls-fixed .sepac-scroll-horizontal-control--left
  padding-right: 8px

.sepac-scroll-horizontal--controls-fixed .sepac-scroll-horizontal-control--right
  padding-left: 8px

.sepac-scroll-horizontal--controls-floating .sepac-scroll-horizontal-control
  position: absolute
  top: 50%
  transform: translateY(-50%)
  z-index: 9

.sepac-scroll-horizontal--controls-floating .sepac-scroll-horizontal-control__button
  background-color: rgba(0, 0, 0, 0.2)

.sepac-scroll-horizontal--controls-floating .sepac-scroll-horizontal-control--left
  left: 20px

.sepac-scroll-horizontal--controls-floating .sepac-scroll-horizontal-control--right
  right: 20px

.sepac-scroll-horizontal--controls-fixed .sepac-scroll-horizontal-control__icon
  color: $primary
  font-weight: bold

.sepac-scroll-horizontal--controls-floating .sepac-scroll-horizontal-control__icon
  color: black

.sepac-scroll-horizontal--controls-invisible.sepac-scroll-horizontal--left-edge
  .sepac-scroll-horizontal-control__button-left
    visibility: hidden

.sepac-scroll-horizontal--controls-invisible.sepac-scroll-horizontal--right-edge
  .sepac-scroll-horizontal-control__button-right
    visibility: hidden

// STILI SCROLLER
// --------------
.sepac-scroll-horizontal-scroller
  overflow-x: hidden

.sepac-scroll-horizontal--scrollbar .sepac-scroll-horizontal-scroller
  overflow-x: scroll

.sepac-scroll-horizontal--draggable .sepac-scroll-horizontal-scroller:hover
  cursor: grab

// STILI SCROLLBAR DELLO SCROLLER
.sepac-scroll-horizontal-scroller
  scrollbar-width: thin
// scrollbar-color: $primary rgba(255, 255, 255, 0)

.sepac-scroll-horizontal-scroller::-webkit-scrollbar-track
  background: $grey-3
  border-radius: 5px

.sepac-scroll-horizontal-scroller::-webkit-scrollbar
  height: 12px

.sepac-scroll-horizontal-scroller::-webkit-scrollbar-thumb
  background-color: $grey-7
  border-radius: 8px
  border: 3px solid $grey-2
</style>
